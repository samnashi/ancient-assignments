PROGRAM MyProgram
	IMPLICIT None

	WRITE(*,*) "My name is Ihsan and my  ID is:"
	WRITE(*,*) " "
	WRITE(*,*) "9"

	STOP

END PROGRAM MyProgram

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
!Prints the prime number array


Remainder = 0

WRITE(*,*) "N: "
READ(*,*) N

IF ((N < 7) .AND. (N > 0)) THEN !First 6 SNP numbers are hardcoded
		Output(1) = 1
		Output(2) = 2
		Output(3) = 3
		Output(4) = 4
		Output(5) = 6
		Output(6) = 11

		do PrintCounter = 1, N
			Write(*,*)Output(PrintCounter)
		end do

	ELSE IF (N < 0) THEN
		WRITE(*,*) "N  is too small."

	ELSE IF (N > 1000) THEN
		Write(*,*) "N is too large."

!only goes in this loop when N is between 7 and 1000
	ELSE IF ((N >= 7).AND.(N < 1001)) THEN
		OutputArrayLocation = 7
		PrimeArrayLocation = 7
		do while (OutputArrayLocation <= N) !the output array contains the SNP numbers
			NotSP = .FALSE.
			Base = 2
				!tries to palindromize the number n (from the prime array)  from base 2 to base n - 2
				do WHILE ((Base <= Prime(PrimeArrayLocation) - 2) .OR. (NotSP .EQV. (.FALSE.)))
					PrimeBeingCompared = Prime(PrimeArrayLocation)
					do while(PrimeBeingCompared > 0) !modified base 10 palindroming algorithm
						NInverse = NInverse * Base
						NInverse = NInverse + MOD(PrimeBeingCompared, Base)
						PrimeBeingCompared = PrimeBeingCompared / Base

						IF ((PrimeBeingCompared == 0) .AND. (Prime(PrimeArrayLocation) == NInverse)) THEN
							NotSP = .TRUE.
						ELSE
							NotSP = .FALSE.
						END IF
					End DO
					Base = Base + 1
				END DO
			!if by the end of this check it is still not palindromizable
			IF (NotSP .EQV. (.FALSE.)) THEN ! the prime is added to the SNP number array
				Output(OutputArrayLocation) = Prime(PrimeArrayLocation)
				PrimeArrayLocation = PrimeArrayLocation + 1
				OutputArrayLocation = OutputArrayLocation + 1
			ELSE !the check tries to check the palindromicity of the next number in the prime array
				PrimeArrayLocation = PrimeArrayLocation + 1
			END IF
		END DO

		WRITE(*,*) "The strictly non palindromic numbers are: "
		DO PrintCounter = 1, N
			WRITE(*,*) Output(PrintCounter)
		END DO
END IF
