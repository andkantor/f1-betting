package andkantor.f1betting.model.calculator;

import andkantor.f1betting.model.bet.Bet;
import andkantor.f1betting.model.bet.Point;
import andkantor.f1betting.model.race.Position;

public class HitCalculator implements PointCalculator {

    @Override
    public Point calculate(Bet bet, CalculationContext context) {
        Position finalPosition = context.getPosition(bet.getDriver());

        if (bet.getFinalPosition().equals(finalPosition)) {
            return Point.HIT;
        }

        return Point.ZERO;
    }
}