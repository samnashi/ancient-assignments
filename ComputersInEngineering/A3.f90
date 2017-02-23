REAL FUNCTION ConvertToCoordinateX(X)
	REAL :: X
	IF (X /= 2160) THEN
		ConvertToCoordinateX = X / 12.0
		ELSE IF (X == 2160) THEN
			ConvertToCoordinateX = 0
	END IF
END FUNCTION ConvertToCoordinateX

REAL FUNCTION ConvertToCoordinateY(Y)
	REAL :: Y
	IF (Y /= 1080) THEN
		ConvertToCoordinateY = Y / 12.0
		ELSE IF (Y == 1080) THEN
			ConvertToCoordinateY = 0
	END IF
END FUNCTIOn ConvertToCoordinateY


PROGRAM A3
IMPLICIT NONE
INTEGER :: loopCounter1, loopCounter2, TopPointElevation, LowestPointElevation
INTEGER :: WaterGridsTotal, i, j
REAL :: TopPointX, TopPointY, CoordinateTopPointX, CoordinateTopPointY, LowestPointX, LowestPointY
REAL :: CoordinateLowestPointX, CoordinateLowestPointY, SumXDistanceTimesMass, SumYDistanceTimesMass
REAL :: SumMass, CentroidX, CentroidY, CoordinateCentroidX, CoordinateCentroidY
REAL :: TotalGrids, WaterMass, RockMass, WaterVolume, WaterSurfaceArea
REAL :: ConvertToCoordinateX, ConvertToCoordinateY
REAL :: AverageElevation, AverageAboveSeaLevelElevation, ElevationTotal
REAL :: AboveSeaLevelElevationTotal, BelowSeaLevelElevationTotal, GridToKmConversionFactor
INTEGER*2 :: Array(2160, 4320)

TopPointElevation = 0
LowestPointElevation = 0
WaterGridsTotal = 0
ElevationTotal = 0
AboveSeaLevelElevationTotal = 0
BelowSeaLevelElevationTotal = 0.0
TotalGrids = (4320 * 2160) !total number of grids
GridToKmConversionFactor = 54.66 ! earth's surface area divided by number of grids in km, this is in sq km
WaterMass = 1.0
RockMass = 2.5
SumXDistanceTimesMass = 0.0
SumYDistanceTimesMass = 0.0
SumMass = 0.0
CentroidX = 0.0
CentroidY = 0.0

OPEN(Unit = 10, FILE = 'tbase.bin', FORM='unformatted', ACCESS='direct', RECL=2*2160*4320)
READ(10,REC=1) ((Array(i, j), i = 1, 2160),j = 1, 4320)

CLOSE(10)

	DO loopCounter1 = 1, 2160 !going left to right, then down
		DO loopCounter2 = 1, 4320

			IF(Array(loopCounter1, loopCounter2) > TopPointElevation) THEN
				TopPointElevation = Array(loopCounter1, loopCounter2)
				TopPointX = loopCounter2
				TopPointY = loopCounter1
			ELSE IF (Array(loopCounter1, loopCounter2) < LowestPointElevation) THEN
				LowestPointElevation = Array(loopCounter1, loopCounter2)
				LowestPointX = loopCounter2
				LowestPointY = loopCounter1
			END IF

			IF((Array(loopCounter1, loopCounter2)) <= 0) THEN
				ElevationTotal = ElevationTotal + Array(loopCounter1, loopCounter2)
				WaterGridsTotal = WaterGridsTotal + 1
				BelowSeaLevelElevationTotal = BelowSeaLevelElevationTotal + Array(loopCounter1, loopCounter2)
			ELSE IF (Array(loopCounter1, loopCounter2) > 0) THEN
				ElevationTotal = ElevationTotal + Array(loopCounter1, loopCounter2)
				AboveSeaLevelElevationTotal = AboveSeaLevelElevationTotal + Array(loopCounter1, loopCounter2)
			END IF

			IF(Array(loopCounter1, loopCounter2) <= 0) THEN
				SumYDistanceTimesMass = SumYDistanceTimesMass + abs(loopCounter1 - 1080) * WaterMass * abs(Array(loopCounter1, loopCounter2))
				SumXDistanceTimesMass = SumXDistanceTimesMass + abs(loopCounter2 - 2160) * WaterMass * abs(Array(loopCounter1, loopCounter2))
				SumMass = SumMass + WaterMass * abs(Array(loopCounter1, loopCounter2))
			ELSE IF (Array(loopCounter1, loopCounter2) > 0) THEN
				SumMass = SumMass + RockMass * abs(Array(loopCounter1, loopCounter2))
				SumYDistanceTimesMass = SumYDistanceTimesMass + abs(loopCounter1 - 1080) * RockMass * abs(Array(loopCounter1, loopCounter2))
				SumXDistanceTimesMass = SumXDistanceTimesMass + abs(loopCounter2 - 2160) * RockMass * abs(Array(loopCounter1, loopCounter2))
			END IF

		END DO
	END DO

	TopPointX = TopPointX - 2160
	TopPointY = TopPointY - 1080
	LowestPointX = LowestPointX - 2160
	LowestPointY = LowestPointY - 1080

	CoordinateTopPointX = ConvertToCoordinateX(TopPointX)
	CoordinateTopPointY = ConvertToCoordinateY(TopPointY)
	CoordinateLowestPointX = ConvertToCoordinateX(LowestPointX)
	CoordinateLowestPointY = ConvertToCoordinateY(LowestPointY)

	AverageElevation = ElevationTotal / TotalGrids
	AverageAboveSeaLevelElevation = AboveSeaLevelElevationTotal / (TotalGrids - WaterGridsTotal)
	!total elevation times number of above sea level grids
	WaterSurfaceArea = WaterGridsTotal / TotalGrids * 100.0 !in percent
	WaterVolume = abs(BelowSeaLevelElevationTotal / 1000) * GridToKmConversionFactor * 10**9
	!area per grid  times sum of underwater cells' depths times number of underwater cells, times a billion because area per grid was km**2

	CentroidX = CentroidX - 2160
	CentroidY = CentroidY - 1080
	CentroidX = SumXDistanceTimesMass / SumMass
	CentroidY = SumYDistanceTimesMass / SumMass
	CoordinateCentroidX = ConvertToCoordinateX(CentroidX)
	CoordinateCentroidY = ConvertToCoordinateY(CentroidY)

OPEN (UNIT = 20, file = 'earthstats.txt')

WRITE(20, *) "Coordinate of highest point (x, y) = ", CoordinateTopPointX, CoordinateTopPointY
WRITE(20, *) "Highest point height (m) = ", TopPointElevation
WRITE(20, *) "Coordinate of lowest point (x, y) = ", CoordinateLowestPointX, CoordinateLowestPointY
WRITE(20, *) "Lowest point height (m) = ", LowestPointElevation
WRITE(20, *) "Centroid coordinate (x, y) = ", CoordinateCentroidX, CoordinateCentroidY
WRITE(20, *) "Average elevation (m) = ", AverageElevation
WRITE(20, *) "Average above-sea-level elevation (m) = ", AverageAboveSeaLevelElevation
WRITE(20, *) "Total volume of water (m**3) = ", WaterVolume
WRITE(20, *) "Percentage of water surface = ", WaterSurfaceArea

CLOSE(20)

END PROGRAM
