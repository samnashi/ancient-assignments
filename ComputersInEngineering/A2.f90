PROGRAM A2
IMPLICIT NONE
INTEGER :: N, NInverse, Remainder, OutputArrayLocation, PrimeArrayLocation
INTEGER :: PrimeArrayMaxIndex, Base, PrimeBeingCompared, Generator, PrintCounter
INTEGER :: Prime(1000), Output(1000)
LOGICAL :: NotPrime, NotSP

!First 10 primes are hardcoded to ensure prime-creating algorithm works
Prime(1) = 1
Prime(2) = 2
Prime(3) = 3
Prime(4) = 5
Prime(5) = 7
Prime(6) = 11
Prime(7) = 13
Prime(8) = 17
Prime(9) = 19
Prime(10) = 23

!Creates the first 1000 prime numbers
PrimeArrayMaxIndex = 10
Generator = 1
Remainder = 0
Do Generator = 28, 7919
	PrimeArrayLocation = 2
	NotPrime = .FALSE.

	Do WHILE ((PrimeArrayLocation <= PrimeArrayMaxIndex) .AND. (NotPrime .EQV. (.FALSE.)))
		Remainder = MOD(Generator, Prime(PrimeArrayLocation))
		!generates values, tries to see whether the generated number is a multiple of any known primes
		If (Remainder == 0) THEN
			NotPrime = .TRUE.
			else
				PrimeArrayLocation = PrimeArrayLocation + 1
		END IF
	End do

	if (NotPrime .EQV. (.FALSE.)) THEN !if the number isn't a multiple of these known primes, then it's added to the prime array
		Prime(PrimeArrayMaxIndex + 1) = Generator
		PrimeArrayMaxIndex = PrimeArrayMaxIndex + 1
	END IF

END DO

WRITE(*,*)"These are the first 10 prime numbers"
Do PrintCounter = 1, 10 !just to show this prime algorithm works
WRITE(*,*)Prime(PrintCounter)
END DO

READ(*,*) N

IF (N < 0) THEN
		WRITE(*,*) "N  is too small."
	ELSE IF (N > 1000) THEN
		Write(*,*) "N is too large."
	ELSE IF ((N < 7) .AND. (N > 0)) THEN !First 6 SNP numbers are hardcoded
		Output(1) = 1
		Output(2) = 2
		Output(3) = 3
		Output(4) = 4
		Output(5) = 6
		Output(6) = 11

		do PrintCounter = 1, N
			WRITE(*,*)"The SNP Numbers are: "
			Write(*,*)Output(PrintCounter)
		end do

	ELSE IF ((N >= 7).AND.(N < 1001)) THEN
		Output(1) = 1
		Output(2) = 2
		Output(3) = 3
		Output(4) = 4
		Output(5) = 6
		Output(6) = 11
		OutputArrayLocation = 7
		PrimeArrayLocation = 7
		do while (OutputArrayLocation <= N) !the output array contains the SNP numbers
			NotSP = .FALSE.
			Base = 2
				!tries to palindromize the number n (from the prime array)  from base 2 to base n - 2
				do WHILE ((Base <= Prime(PrimeArrayLocation) - 2) .AND. (NotSP .EQV. (.FALSE.)))
					PrimeBeingCompared = Prime(PrimeArrayLocation)
					NInverse = 0
					do while(PrimeBeingCompared > 0) !modified base 10 palindroming algorithm
						NInverse = NInverse * Base
						NInverse = NInverse + MOD(PrimeBeingCompared, Base)
						PrimeBeingCompared = PrimeBeingCompared / Base
					End DO
					IF ((PrimeBeingCompared == 0) .AND. (Prime(PrimeArrayLocation) == NInverse)) THEN
							NotSP = .TRUE.
						ELSE
							NotSP = .FALSE.
						END IF
					Base = Base + 1
				END DO
				NInverse = 0
			!if by the end of this check it is still not palindromizable
			IF (NotSP .EQV. (.FALSE.)) THEN ! the prime is added to the SNP number array
				Output(OutputArrayLocation) = Prime(PrimeArrayLocation)
				PrimeArrayLocation = PrimeArrayLocation + 1
				OutputArrayLocation = OutputArrayLocation + 1
			ELSE !the check tries to check the palindromicity of the next number in the prime array0
				PrimeArrayLocation = PrimeArrayLocation + 1
			END IF
		END DO

		WRITE(*,*) "The strictly non palindromic numbers are: "
		DO PrintCounter = 1, N
			WRITE(*,*) Output(PrintCounter)
		END DO

END IF

END PROGRAM
