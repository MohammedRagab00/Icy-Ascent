package TheGame;

import Models.GameObject;
import java.util.List;

public class CollisionManager {

    public static void remove(RiverRaidGLEventListener g) {
        removeMarkedObjects(g.gameState.plans);
        removeMarkedObjects(g.gameState.ships);
        removeMarkedObjects(g.gameState.homes);
        removeMarkedObjects(g.gameState.fuelTanks);
    }

    private static <T extends GameObject> void removeMarkedObjects(List<T> list) {
        list.removeIf(obj -> obj.remove);
    }

    public static void destroy(RiverRaidGLEventListener g) {
        checkPlanCollisions(g);
        checkShipCollisions(g);
        checkBoundaryCollisions(g);
        checkFuelTankCollisions(g);
    }

    private static void checkPlanCollisions(RiverRaidGLEventListener g) {
        for (GameObject plan : g.gameState.plans) {
            double bulletDistance = calcD(plan.x, g.gameState.xBullet, plan.y, g.gameState.yBullet);
            double playerDistance = calcD(plan.x, g.gameState.x, plan.y, g.gameState.y);
            if (g.gameState.fired && bulletDistance < 5) {
                plan.remove = true;
                g.gameState.fired = false;
                g.gameState.setCurrentPlayerScore(g.gameState.getCurrentPlayerScore() + 50);
            }
            if (playerDistance < 7) {
                g.crashed();
            }
        }
    }

    private static void checkShipCollisions(RiverRaidGLEventListener g) {
        for (GameObject ship : g.gameState.ships) {
            double bulletDistance = calcD(ship.x, g.gameState.xBullet, ship.y, g.gameState.yBullet);
            double playerDistance = calcD(ship.x, g.gameState.x, ship.y, g.gameState.y);
            if (g.gameState.fired && bulletDistance < 5) {
                ship.remove = true;
                g.gameState.fired = false;
                g.gameState.setCurrentPlayerScore(g.gameState.getCurrentPlayerScore() + 100);
            }
            if (playerDistance < 10) {
                g.crashed();
            }
        }
    }

    private static void checkBoundaryCollisions(RiverRaidGLEventListener g) {
        if (g.gameState.x > 70 || g.gameState.x < 20) {
            g.crashed();
        }
    }

    private static void checkFuelTankCollisions(RiverRaidGLEventListener g) {
        for (GameObject fuelTank : g.gameState.fuelTanks) {
            if (isWithinBounds(g.gameState.x, g.gameState.y, fuelTank)) {
                g.gameState.tank = Math.min(g.gameState.tank + 20, 100);
            }
            if (isWithinBounds(g.gameState.xBullet, g.gameState.yBullet, fuelTank)) {
                fuelTank.remove = true;
                g.gameState.fired = false;
                g.gameState.setCurrentPlayerScore(g.gameState.getCurrentPlayerScore() + 100);
            }
        }
    }

    private static boolean isWithinBounds(int x, int y, GameObject fuelTank) {
        return x > fuelTank.x - 2 && x < fuelTank.x + 2
                && y > fuelTank.y - 8 && y < fuelTank.y + 5;
    }

    public static double calcD(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
