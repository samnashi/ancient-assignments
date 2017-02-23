#include "stdio.h"
#include "stdlib.h"
#include "math.h"


typedef double (*DfDDP)(double, double, double, double (*rateEqPtr)(double, double));

double simpsons_int(DfDDP f, double (*rateEqPtr)(double, double), double x0, double x1, int n, double F, double C)
{
  if (n == 0){
		return 0;
	} // so i don't get an infinite volume when XPFR is 0

	double x, sum, dx = (x1 - x0) / n;
	sum = f(F, x1, C, rateEqPtr) - f(F, x0, C, rateEqPtr);

	for(x = x0; x+dx/2 < x1; x += dx){
		sum += 2.0 * f(F, x, C, rateEqPtr) +4.0 * f(F, x + dx/2, C, rateEqPtr);
	}
	return sum * dx / 6.0;
}

double rate(double c, double x){
	return ((-10.0 * c * (1.0 - x)) / (6.5712 + 12 * c * c * (1 - x) * (1 - x)));
}

double vol_cstr(double x1, double x2, double F, double C, double (*rateEqPtr)(double, double))
{
	if (x2 == 0){
		return 0.0;
	} // XCSTR = 0, VCSTR = 0
	return ((-F * (x2 - x1)) / ((*rateEqPtr)(C, x2)));
}

double integrand (double F, double x, double C, double (*rateEqPtr)(double, double)){
	return (-F) / ((*rateEqPtr)(C, x));
}

void main()
{
	double x_cstr, v_cstr, x_pfr, v_pfr, v_total;
	double min_vol, xi;
	double min_x_pfr = 0.0;
	double min_x_cstr = 0.0;
	double min_pfr_vol = 0.0;
	double min_cstr_vol = 0.0;
	int setup;

	double F, C, XTotal, XStep, XStepInt;
	scanf("%lf, %lf, %lf, %lf, %lf", &F, &C, &XTotal, &XStep, &XStepInt);
	FILE *OutputFile;
	OutputFile = fopen("ReacSetup.txt", "w");
	if(!OutputFile){
		printf("can't open file.");
	}

	setup = 1;
	double (*rateEqPtr) (double, double) = rate;
	fprintf(OutputFile, "Layout 1: PFR then CSTR \n");
	min_vol = vol_cstr(0.0, XTotal, F, C, rateEqPtr);
	for(xi = 0.0; xi <= XTotal; xi = xi + XStep)
	{
		v_pfr = simpsons_int(integrand, rateEqPtr, xi, XTotal, ((XTotal - xi) / XStepInt), F, C);
		v_cstr = vol_cstr(0.0, xi, F, C, rateEqPtr);
		x_pfr = XTotal - xi;
		x_cstr = xi;
		v_total = v_cstr + v_pfr;

		if (v_total < min_vol)
		{
			min_vol = v_total;
			min_cstr_vol = v_cstr;
			min_pfr_vol = v_pfr;
			min_x_pfr = x_pfr;
			min_x_cstr = x_cstr;
		}
		fprintf(OutputFile, "Vol.CSTR = %.2lf, Vol.PFR = %.2lf, XCSTR = %.2lf, XPFR = %.2lf, Total = %.2lf \n"
		, v_cstr, v_pfr, x_cstr, x_pfr, v_total);
	}
	xi = 0;

	fprintf(OutputFile, "\n");
	fprintf(OutputFile, "Layout 2: CSTR then PFR \n");
	v_cstr = vol_cstr(0, XTotal, F, C, rateEqPtr);
	for(xi = 0.0; xi <= XTotal; xi = xi + XStep)
	{
		v_pfr = simpsons_int(integrand, rateEqPtr, 0, xi, xi/XStepInt, F, C);
		v_cstr = vol_cstr(xi, XTotal, F, C, rateEqPtr);
		v_total = v_cstr + v_pfr;
		x_pfr = xi;
		x_cstr = XTotal - xi;
		if (v_total < min_vol)
		{
			min_vol = v_total;
			min_cstr_vol = v_cstr;
			min_pfr_vol = v_pfr;
			min_x_pfr = x_pfr;
			min_x_cstr = x_cstr;
			setup = 2;
		}
		fprintf(OutputFile, "Vol.CSTR = %.2lf, Vol.PFR = %.2lf, XPFR = %.2lf, XCSTR = %.2lf, Total = %.2lf \n"
		, v_cstr, v_pfr, x_pfr, x_cstr, v_total);
	}
	fprintf(OutputFile, "\n");
	fprintf(OutputFile, "Optimum Setup: (F = %.2lf, C = %.2lf, X_Total = %.2lf, X_Step = %.2lf, XStepInt = %.2lf) \n",
	F, C, XTotal, XStep, XStepInt);
	fprintf(OutputFile, "Layout %i, CSTR Vol = %.2lf, PFR Vol = %.2lf \n", setup, min_cstr_vol, min_pfr_vol);
	fprintf(OutputFile, "XCSTR = %.2lf, XPFR = %.2lf, Total Min Volume = %.2lf \n", min_x_cstr, min_x_pfr, min_vol);

	fclose(OutputFile);
}