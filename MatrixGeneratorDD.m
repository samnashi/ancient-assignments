clc;
rng(1)
n=2048;
mmax=100;
mat1=100*mmax*rand(n)-mmax;
mat=0.001*mat1+999*diag(sum(abs(mat1),2).*sign(diag(mat1)));

fid = fopen('H:\UI Comp Sci Work\Parallel\Assignment1\MatrixDD.txt', 'w');
fprintf(fid, '%d %d\n', size(mat,1), size(mat,2));   
for i=1:size(mat,1)
    for j=1:size(mat,2)
        fprintf(fid, '%0.3f\n', mat(i,j));
    end
end
    fclose(fid);
    
vect = randi([-999999999,999999999],n,1);
fid2 = fopen('H:\UI Comp Sci Work\Parallel\Assignment1\RandomVector.txt', 'w');
fprintf(fid, '%d\n', size(vect,1));  
for i=1:size(vect,1)
    fprintf(fid2, '%0.3f', vect(i));
    fprintf(fid2,'\n');
end
fclose(fid2);
