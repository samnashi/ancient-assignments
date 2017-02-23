#include "stdio.h"
#include "stdlib.h"
#include "time.h"
#include <stdbool.h>

void main()
{
	int *a1,*a2, *a3, *a4, *a5, *a6;
	int *b2, *b3, *b4, *b5, *b6;
	int *c12, *c13, *c14, *c15, *c16;
	int i, j;
	int cyclesTotal = 0;
	int inputCycles;
	bool noDiversity = false;
	bool noDiversity1 = false; // for first column
	bool noDiversity2 = false; // for second column, etc.
	bool noDiversity3 = false;
	bool noDiversity4 = false;
	bool noDiversity5 = false;

	int specimen1[5], specimen2[5], specimen3[5], specimen4[5], specimen5[5], specimen6[5];
	int newSpecimen2[5], newSpecimen3[5], newSpecimen4[5], newSpecimen5[5], newSpecimen6[5];
	int cSpecimen12[10], cSpecimen13[10], cSpecimen14[10], cSpecimen15[10], cSpecimen16[10];

	a1 = specimen1;
	a2 = specimen2;
	a3 = specimen3;
	a4 = specimen4;
	a5 = specimen5;
	a6 = specimen6;

	b2 = newSpecimen2;
	b3 = newSpecimen3;
	b4 = newSpecimen4;
	b5 = newSpecimen5;
	b6 = newSpecimen6;

	c12 = cSpecimen12; // "gene pool" between specimens 1 and 2
	c13 = cSpecimen13;
	c14 = cSpecimen14;
	c15 = cSpecimen15;
	c16 = cSpecimen16;

  srand (time(NULL)); // initialize seed
	scanf("%i", &inputCycles);

	for(i = 0; i < 5; i++) // fill specimens
	{
		specimen1[i] = (rand() % 100);
		specimen2[i] = (rand() % 100);
		specimen3[i] = (rand() % 100);
		specimen4[i] = (rand() % 100);
		specimen5[i] = (rand() % 100);
		specimen6[i] = (rand() % 100);
	}

	printf("Specimen 1: "); // prints initial specimen values
	for(i = 0; i < 5; i++){
		printf("[%i]", specimen1[i]);
	}
	printf("\n");
	printf("Specimen 2: ");
	for(i = 0; i < 5; i++){
		printf("[%i]", specimen2[i]);
	}
	printf("\n");
	printf("Specimen 3: ");
	for(i = 0; i < 5; i++){
		printf("[%i]", specimen3[i]);
	}
	printf("\n");
	printf("Specimen 4: ");
	for (i = 0; i < 5; i++){
		printf("[%i]", specimen4[i]);
	}
	printf("\n");
	printf("Specimen 5: ");
	for(i = 0; i < 5; i++){
		printf("[%i]", specimen5[i]);
	}
	printf("\n");
	printf("Specimen 6: ");
	for(i = 0; i < 5; i++){
		printf("[%i]", specimen6[i]);
	}

	for(j = 0; j < 9; j = j + 2) // fills even indices in all the "gene pool" arrays with specimen 1 chromosomes
	{
		if (j == 0){
			*c12 = *c13 = *c14 = *c15 = *c16 = *a1;
		}
		else{
		*(c12 + j) = *(c13 + j) = *(c14 + j) = *(c15 + j) = *(c16 + j) = *(a1 + (j/2));
		}
	}

	for(j = 1; j < 10; j = j + 2) // fills odd indices in the gene pool with the respective specimen's chromosomes
	{
		if(j == 1){
			*(c12 + j) = *a2;
			*(c13 + j) = *a3;
			*(c14 + j) = *a4;
			*(c15 + j) = *a5;
			*(c16 + j) = *a6;
		}
		else if(j == 3){
			*(c12 + j) = *(a2 + 1);
			*(c13 + j) = *(a3 + 1);
			*(c14 + j) = *(a4 + 1);
			*(c15 + j) = *(a5 + 1);
			*(c16 + j) = *(a6 + 1);
		}
		else if(j == 5){
			*(c12 + j) = *(a2 + 2);
			*(c13 + j) = *(a3 + 2);
			*(c14 + j) = *(a4 + 2);
			*(c15 + j) = *(a5 + 2);
			*(c16 + j) = *(a6 + 2);
		}
		else if(j == 7){
			*(c12 + j) = *(a2 + 3);
			*(c13 + j) = *(a3 + 3);
			*(c14 + j) = *(a4 + 3);
			*(c15 + j) = *(a5 + 3);
			*(c16 + j) = *(a6 + 3);
		}
		else if(j == 9){
			*(c12 + j) = *(a2 + 4);
			*(c13 + j) = *(a3 + 4);
			*(c14 + j) = *(a4 + 4);
			*(c15 + j) = *(a5 + 4);
			*(c16 + j) = *(a6 + 4);
		}
	}
	printf("\n");
	printf("\n");
	printf("Specimen 1+2: ");
	for(i = 0; i < 10; i++){
		printf("[%i]", cSpecimen12[i]);
	}
	printf("\n");
	printf("Specimen 1+3: ");
	for(i = 0; i < 10; i++){
		printf("[%i]", cSpecimen13[i]);
	}
	printf("\n");
	printf("Specimen 1+4: ");
	for(i = 0; i < 10; i++){
		printf("[%i]", cSpecimen14[i]);
	}
	printf("\n");
	printf("Specimen 1+5: ");
	for (i = 0; i < 10; i++){
		printf("[%i]", cSpecimen15[i]);
	}
	printf("\n");
	printf("Specimen 1+6: ");
	for(i = 0; i < 10; i++){
		printf("[%i]", cSpecimen16[i]);
	}
	printf("\n");

	while(noDiversity == false)
	{
		for(j = 1; j < 10; j = j + 2) // fills odd indices in the gene pool with the respective specimen's chromosomes
		{
			if(j == 1){
				*(c12 + j) = *a2;
				*(c13 + j) = *a3;
				*(c14 + j) = *a4;
				*(c15 + j) = *a5;
				*(c16 + j) = *a6;
			}
			else if(j == 3){
				*(c12 + j) = *(a2 + 1);
				*(c13 + j) = *(a3 + 1);
				*(c14 + j) = *(a4 + 1);
				*(c15 + j) = *(a5 + 1);
				*(c16 + j) = *(a6 + 1);
			}
			else if(j == 5){
				*(c12 + j) = *(a2 + 2);
				*(c13 + j) = *(a3 + 2);
				*(c14 + j) = *(a4 + 2);
				*(c15 + j) = *(a5 + 2);
				*(c16 + j) = *(a6 + 2);
			}
			else if(j == 7){
				*(c12 + j) = *(a2 + 3);
				*(c13 + j) = *(a3 + 3);
				*(c14 + j) = *(a4 + 3);
				*(c15 + j) = *(a5 + 3);
				*(c16 + j) = *(a6 + 3);
			}
			else if(j == 9){
				*(c12 + j) = *(a2 + 4);
				*(c13 + j) = *(a3 + 4);
				*(c14 + j) = *(a4 + 4);
				*(c15 + j) = *(a5 + 4);
				*(c16 + j) = *(a6 + 4);
			}
		}
		for(i = 0; i < 5; i++) // new specimens are made from gene pool
		{
			newSpecimen2[i] = cSpecimen12[(rand() % 2) + 2*i];
			newSpecimen3[i] = cSpecimen13[(rand() % 2) + 2*i];
			newSpecimen4[i] = cSpecimen14[(rand() % 2) + 2*i];
			newSpecimen5[i] = cSpecimen15[(rand() % 2) + 2*i];
			newSpecimen6[i] = cSpecimen16[(rand() % 2) + 2*i];
		}
		cyclesTotal++;
		if(cyclesTotal <= inputCycles)
		{
			printf("\nCycle number: %i \n", cyclesTotal);
			printf("\nNew Specimen 1: "); // prints contents of new specimens
			for(i = 0; i < 5; i++){
				printf("[%i]", specimen1[i]);
			}
			printf("\n");
			printf("New Specimen 2: ");
			for(i = 0; i < 5; i++){
				printf("[%i]", *(b2 + i));
			}
			printf("\n");
			printf("New Specimen 3: ");
			for(i = 0; i < 5; i++){
				printf("[%i]", *(b3 + i));
			}
			printf("\n");
			printf("New Specimen 4: ");
			for (i = 0; i < 5; i++){
				printf("[%i]", *(b4 + i));
			}
			printf("\n");
			printf("New Specimen 5: ");
			for(i = 0; i < 5; i++){
				printf("[%i]", *(b5 + i));
			}
			printf("\n");
			printf("New Specimen 6: ");
			for(i = 0; i < 5; i++){
				printf("[%i]", *(b6 + i));
			}
			printf("\n");
		}

		for(i = 0; i < 5; i++)
		{
			if ((specimen1[i] == newSpecimen2[i]) && (specimen1[i] == newSpecimen3[i]) && (specimen1[i] == newSpecimen4[i])
			&& (specimen1[i] == newSpecimen5[i]) && (specimen1[i] == newSpecimen6[i]))
			{
				if(i == 0){
					noDiversity1 = true;
				}
				if(i == 1){
					noDiversity2 = true;
				}
				if(i == 2){
					noDiversity3 = true;
				}
				if(i == 3){
					noDiversity4 = true;
				}
				if(i == 4){
					noDiversity5 = true;
				}
			}
			else{
				noDiversity = false;
			}
		}

		if((noDiversity1 == true) && (noDiversity2 == true) && (noDiversity3 == true) && (noDiversity4 = true)
		&& (noDiversity5 == true)){
			noDiversity = true; // all columns are the same
		}

		if(noDiversity == false)
		{
			noDiversity = false;
			for(i = 0; i < 5; i++)// deletes original specimen data, replaced with new specimen data
			{
				*(a2 + i) = *(b2 + i);
				*(a3 + i) = *(b3 + i);
				*(a4 + i) = *(b4 + i);
				*(a5 + i) = *(b5 + i);
				*(a6 + i) = *(b6 + i);
			}
		}
	}
	printf("\nTotal Cycles performed until diversity is gone: %i \n", cyclesTotal);
}