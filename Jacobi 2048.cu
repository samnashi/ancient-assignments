

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <iostream>
#include <fstream>
#include <thrust/device_vector.h>

using namespace std;

#define ERROR_TOL 0.0001


__host__ int checkIfDD(int numUnknowns, float mat[])
{
	int   diagCounter=0, offDiagCounter=0, dd=0;
	float chkdd=0;
	float sumdd=0;
	//int *nonDDRows;

	//nonDDRows = (int *)malloc(numUnknowns * sizeof(int *));

	printf("\nChecking if the matrix is (strictly) diagonally dominant...\n");

	//ROW-WISE diagonally dominant check 
	for (diagCounter = 0; diagCounter < numUnknowns*numUnknowns; diagCounter =  diagCounter + numUnknowns+1)
	{ 
		chkdd = 0;
		chkdd = mat[diagCounter];//diagonal terms
		//printf("chkdd [diagCounter] %2d %i \n", chkdd, diagCounter);
		sumdd = 0;
		offDiagCounter = 1;
		for (offDiagCounter = diagCounter; offDiagCounter < (diagCounter + numUnknowns) && offDiagCounter <= (numUnknowns*numUnknowns); offDiagCounter = offDiagCounter + 1)
		{
			//printf(" in loop sumdd [offDiagCounter] %2d %i \n", sumdd, offDiagCounter);
			if (offDiagCounter != diagCounter){ 
				sumdd = sumdd + mat[offDiagCounter]; }
			//printf(" in if statement sumdd [offDiagCounter] %2d %i \n", sumdd, offDiagCounter);
		}
			if (sumdd <= chkdd){ dd++; }
	}
	if (dd == numUnknowns){
		printf(
			"\nYES ..."
			"\nThe matrix is (strictly) diagonally dominant.");
	}
	else{
		printf(
			"\nThe matrix is NOT (strictly) diagonally dominant.\nOnly %i rows are DD.\n",dd);
		return 0; /* false */
	}
	return 1; /* true */
}

__global__ void add(float *a_d,
	float *b_d,
	float * c_d,
	int dim)
{
	int tid = threadIdx.x + blockIdx.x * blockDim.x;
	while (tid < dim)
	{
		c_d[tid] = a_d[tid] + b_d[tid];
		tid += gridDim.x * blockDim.x;
	}
}

__global__ void substract(float *a_d,
	float *b_d,
	float *c_d,
	int dim)
{
	int tid = threadIdx.x + blockIdx.x * blockDim.x;
	while (tid < dim)
	{
		c_d[tid] = a_d[tid] - b_d[tid];
		tid += gridDim.x * blockDim.x;
	}
}

// this function is specifically made for the jacobi iterations.
// it will multiple vec[i] by frac{1}{diag[i]}

__global__ void diaMultVec(float * diag, float * vec, int dim)
{
	int tid = threadIdx.x + blockIdx.x * blockDim.x;
	while (tid < dim)
	{
		if (diag[tid] != 0)
		{
			vec[tid] /= diag[tid];
		}
		tid += gridDim.x * blockDim.x;
	}

}

// VecAbs() is the function to make Vec only contain the absolute value

__global__ void VecAbs(float * vec, int dim)
{
	int tid = threadIdx.x + blockIdx.x * blockDim.x;
	while (tid < dim)
	{
		if (vec[tid] < 0)
		{
			vec[tid] = -vec[tid];
		}
		tid += gridDim.x * blockDim.x;
	}

}
__host__ int getMatDim(FILE *fpMatA){
	int Matrix_Size[2];
	int NoofRows_A = 0, NoofCols_A = 0;
	fscanf(fpMatA, "%d %d\n", &NoofRows_A, &NoofCols_A);
	Matrix_Size[0] = NoofRows_A;
	Matrix_Size[1] = NoofCols_A;
	return Matrix_Size[0];
}

__host__ float** getVector(FILE *fpVectB, int sysDim)
{
	int irow = 0;
	float ** vectB;
	//vectB = new double[sysDim];
	vectB = (float **)malloc(sysDim * sizeof(float*));
		for (irow = 1; irow < sysDim+1; irow++){
			fscanf(fpVectB, "%f", &vectB[irow]);
	}
		return vectB;
}
// Make the VecMax just step by step to avoid __syncblocks()

__global__ void SwapForOddDim(float * vec, int dim)
{
	if (vec[0] < vec[dim - 1])
		vec[0] = vec[dim - 1];
}


__global__ void VecMaxOneStepCompare(float * vec, int dim)
{
	int tid = threadIdx.x + blockIdx.x * blockDim.x;
	int mid = dim / 2; // get the half size

	while (tid < mid)
	{
		if (tid < mid)    // filter the active thread
		{
			if (vec[tid] < vec[tid + mid]) // get the larger one between vec[tid] and vec[tid+mid]
				vec[tid] = vec[tid + mid];  // and store the larger one in vec[tid]
		}

		tid += gridDim.x * blockDim.x;
	}
	__syncthreads();
}
	
// VecMax() is the norm of a vector. Since we are using infinity norm
// we just call it VecMax instead of VecNorm.
// dim:  the size vector
// the max value is stored at vec[0]
// the content of vec will be changed

void VecMax(float * vec, int dim, int dimB, int dimT)
{

	while (dim > 1)
	{
		VecMaxOneStepCompare << <dimB, dimT >> >(vec, dim);

		// if the dimension is odd.
		if (dim % 2)
		{
			SwapForOddDim << <1, 1 >> >(vec, dim);
		}
		dim /= 2;        // make the vector half size short.
	}

	// compare last two values
	SwapForOddDim << <1, 1 >> >(vec, 2);
}



// The following fucntion does the job of matrix-vector multiplication.
// Well, in this homework, although we can assume the matrix is square,
// the matMultVec is written for a more general case.
// 
// mat_A:  A dim_row by dim_col matrix which is stored in row-major manner.
// vec:    A dim_col dimension vector.
// rst:    The result dim_row dimension vector.

__global__ void matMultVec(float * mat_A,
	float * vec,
	float * rst,
	int dim_row,
	int dim_col)
{
	int rowIdx = threadIdx.x + blockIdx.x * blockDim.x; // Get the row Index 
	int aIdx;
	while (rowIdx < dim_row)
	{
		rst[rowIdx] = 0; // clean the value at first
		for (int i = 0; i < dim_col; i++)
		{
			aIdx = rowIdx * dim_col + i; // Get the index for the element a_{rowIdx, i}
			rst[rowIdx] += (mat_A[aIdx] * vec[i]); // do the multiplication
		}
		rowIdx += gridDim.x * blockDim.x;
	}
	__syncthreads();
}


/*******************************
function jacobi (GPU Version):
The following function does the main things.

A X = B

A: the dim by dim matrix
B: the dim dimenstion vector
X: the solution

*******************************/

void jacobi(float * A, float * B, float * X, int dim)
{
	float * diag;   // 1D array: a vector with diagonal elements from A
	float * LU;     // 1D array: a matrix with off-diagonal elements from A, row major(?)
	float * x_old;

	diag = new float[dim];
	LU = new float[dim*dim];
	x_old = new float[dim];

	// initialize diag, LU
	for (int i = 0; i < dim; i++)
	{
		diag[i] = A[i*dim + i];
		for (int j = 0; j < dim; j++)
		{
			LU[i*dim + j] = A[i*dim + j];
		}
		LU[i*dim + i] = 0;
	}

	// initialize the block dimension and thread dimension.
	// find the best dimB and dimT to fit the dim
	int dimB, dimT;
	dimT = 8; //originally 256
	dimB = (dim / dimT) + 1;
	printf("\nThread size = %i, # of Blocks = %i \n", dimT, dimB);

	float err = 1.0, checkDotSum = 0;

	// set up the memory for GPU
	float * LU_d;
	float * B_d;
	float * diag_d;
	float *X_d, *X_old_d;
	float * tmp;
	float * tmp_h;

	tmp_h = (float *)malloc(dim * sizeof(float *)); //for final dot product check
	cudaMalloc((void **)&B_d, sizeof(float) * dim);
	cudaMalloc((void **)&diag_d, sizeof(float) * dim);
	cudaMalloc((void **)&LU_d, sizeof(float) * dim * dim);

	cudaMemcpy(LU_d, LU, sizeof(float) * dim * dim, cudaMemcpyHostToDevice);
	cudaMemcpy(B_d, B, sizeof(float) * dim, cudaMemcpyHostToDevice);
	cudaMemcpy(diag_d, diag, sizeof(float) * dim, cudaMemcpyHostToDevice);

	cudaMalloc((void **)&X_d, sizeof(float) * dim);
	cudaMalloc((void **)&X_old_d, sizeof(float) * dim);
	cudaMalloc((void **)&tmp, sizeof(float) * dim);

	float * max;
	max = new float;
	int count = 0;
	while (err > ERROR_TOL) // do the iteration untill err is less than tolerance
	{
		count++;
		// 1. Copy X to x_old
		for (int i = 0; i < dim; i++){
			x_old[i] = X[i];
		}
		// 2. Compute X by A x_old

		cudaMemcpy(X_old_d, x_old, sizeof(float) * dim, cudaMemcpyHostToDevice);
		matMultVec << <dimB, dimT >> >(LU_d, X_old_d, tmp, dim, dim); // use x_old to compute LU X_old and store the result in tmp
		substract << <dimB, dimT >> >(B_d, tmp, X_d, dim);        // get the (B - LU X_old), which is stored in X_d
		diaMultVec << <dimB, dimT >> >(diag_d, X_d, dim);         // get the new X

		// 3. copy the new X back to the Host Memory
		cudaMemcpy(X, X_d, sizeof(float) * dim, cudaMemcpyDeviceToHost);

		// 4. calculate the norm of X_new - X_old
		substract << <dimB, dimT >> >(X_old_d, X_d, tmp, dim);
		VecAbs << <dimB, dimT >> >(tmp, dim);
		VecMax(tmp, dim, dimB, dimT);

		// copy the max value from Device to Host

		cudaMemcpy(max, tmp, sizeof(float), cudaMemcpyDeviceToHost);
		err = (*max);
		printf("\nIteration #%i, Error: %f", count, err);
	}

	cout<<"\n\nThis converged after "<<count<<" iterations.\n \n";

	//Check for correctness
	//initialize tmp_h 
	for (int i = 0; i < dim; i++){
		tmp_h[i] = 0;
	}
	matMultVec << <dimB, dimT >> >(A, X, tmp_h, dim, dim); //tmp stores Ax
	for (int i = 0; i < dim; i++){
		checkDotSum += tmp_h[i] * B[i];
	}//check (Ax) dot B 
	printf("Check: [Ax] dot [B] = %f \n \n", checkDotSum);

	// free memory after all iterations
	cudaFree(LU_d);
	cudaFree(B_d);
	cudaFree(diag_d);
	cudaFree(tmp);
	cudaFree(X_old_d);
	cudaFree(X_d);
	delete[] diag;
	delete[] LU;
	delete[] x_old;
	delete max;

}

double test(int n)
{
	//double **mat;
	//float * A; //probably needs to be deleted. 
	//float * B;
	float * vectX; 
	float *vectB;
	float *matA;
	int vectDim=0;
	float initialScalingFactor = 1; 
	
	FILE *fpM;
	fpM = fopen("H:\\UI Comp Sci Work\\Parallel\\Assignment1\\MatrixDD.txt", "r");//DOUBLE SLASH FOR ADDRESSES
	printf("\nGetting system dimension...");
	int dim = getMatDim(fpM);
	printf("\nSystem dimension is %i.\n", dim);
	
	int irow = 0;
	matA = new float[dim*dim];
	for (irow = 0; irow < dim * dim + 1; irow++){
		//if (irow > 1){ fscanf(fpM, "%f\n", &matA[irow]); } //ROW MAJOR
		fscanf(fpM, "%f\n", &matA[irow]);
		//printf("%f\n",matA[irow]);
	}
	fclose(fpM);
	printf("\nSuccessfully read the matrix file.");

	checkIfDD(dim, matA);

	FILE *fpV;
	fpV = fopen("H:\\UI Comp Sci Work\\Parallel\\Assignment1\\RandomVector.txt", "r");

	fscanf(fpV, "%f\n", &vectDim);//checks if vectDim is the same dimension

	vectB = new float [dim];
	vectX = new float[dim];
	irow = 0;
	for (irow = 0; irow < dim + 1; irow++){
		//if (irow > 1){fscanf(fpV, "%f\n", &vectB[irow]); }
		fscanf(fpV, "%f\n", &vectB[irow]);
		vectX[irow] = initialScalingFactor * vectB[irow]; //initial guess
		//printf("%f\n",vectB[irow]);
	}
	printf("\nSuccessfully read the vector file.");
	printf("\nInitialized x to be %.2f * b.",initialScalingFactor);
	//getVector(fpV, dim);

	fclose(fpV);

	//get the time
	clock_t start, finish;
	double totaltime;
	start = clock();

	printf("\n\nRunning Jacobi iteration...");
	jacobi(matA, vectB, vectX, dim);  // do the jacobi

	finish = clock();
	totaltime = (double)(finish - start) / CLOCKS_PER_SEC;

	//if (c == 'y')
	//{
	//	for (int i = 0; i < dim; i++)
	//	{
	//		cout << vectX[i] << endl;
	//	}
	//}

	FILE *fpR; //result vector
	fpR = fopen("H:\\UI Comp Sci Work\\Parallel\\Assignment 3\\JacobiResultVector.txt", "w+");//double check path. 
	if (fpR != NULL){
		for (irow = 0; irow < dim; irow++){
			fprintf(fpR,"%f\n",vectX[irow]);
		}
	}
	fclose(fpR);

	FILE *fpRP; //performance results
	fpRP = fopen("H:\\UI Comp Sci Work\\Parallel\\Assignment 3\\PerformanceResults.txt", "w+");//double check path. 
	if (fpRP != NULL){
			fprintf(fpRP, "%d\n", totaltime);
	}
	else printf("Can't write the result vector. Check the path or filename");
	

	delete[] matA;
	delete[] vectB;
	delete[] vectX;
	return totaltime;
}


int main(int argc, char *argv[])
{
	
	int size = 2048;
	double t = test(size);
	cout << "Wall time:" << t << " secs" << endl;
	printf("\nSee JacobiResultVector.txt for X.");
	return 0;
}

