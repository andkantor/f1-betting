package andkantor.f1betting.model;

import andkantor.f1betting.entity.CumulativePoint;
import andkantor.f1betting.entity.Point;
import andkantor.f1betting.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    private List<User> users;
    private Map<User, CumulativePoint> cumulativePoints;
    private List<Point> points = new ArrayList<>();

    public Leaderboard(List<User> users, Map<User, CumulativePoint> cumulativePoints) {
        this.users = users;
        this.cumulativePoints = cumulativePoints;

        this.users.sort((user1, user2) -> compareUsers(user1, user2, cumulativePoints));
        this.users.forEach(user -> points.add(cumulativePoints.get(user).getPoint()));
    }

    public List<User> getUsers() {
        return users;
    }

    public int getPlace(User user) {
        return points.indexOf(getPoints(user)) + 1;
    }

    public Point getPoints(User user) {
        return cumulativePoints.get(user).getPoint();
    }

    private int compareUsers(User user1, User user2, Map<User, CumulativePoint> cumulativePoints) {
        int compareByPoints = cumulativePoints.get(user1).getPoint()
                .compareTo(cumulativePoints.get(user2).getPoint());

        return compareByPoints == 0
                ? user1.getUsername().compareTo(user2.getUsername())
                : compareByPoints;
    }
}
