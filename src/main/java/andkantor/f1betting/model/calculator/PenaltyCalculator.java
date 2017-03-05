package andkantor.f1betting.model.calculator;

import andkantor.f1betting.model.bet.Bet;
import andkantor.f1betting.model.bet.Penalty;
import andkantor.f1betting.model.bet.Point;

import java.util.Optional;

public class PenaltyCalculator implements PointCalculator {

    @Override
    public Point calculate(Bet bet, CalculationContext context) {
        Optional<Penalty> penalty = context.getPenalty(bet.getDriver(), bet.getFinalPosition());

        if (penalty.isPresent()) {
            return penalty.get().getPoint();
        }

        return Point.ZERO;
    }
}