import java.util.HashMap;
import java.util.Scanner;

public class Task5 {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        HashMap<Integer, Adventurer> adventurers = new HashMap<>();
        int m = SCANNER.nextInt();
        while (m-- > 0) {
            int operation = SCANNER.nextInt();
            Integer advId = SCANNER.nextInt();
            switch (operation) {
                case 1: {
                    Adventurer adv = new Adventurer(advId, SCANNER.next());
                    adventurers.put(advId, adv);
                    break;
                }
                case 2: {
                    int eqpType = SCANNER.nextInt();
                    Equipment equipment = doOperationTwo(eqpType);
                    if (equipment != null) {
                        adventurers.get(advId).addValueBody(equipment);
                    }
                    break;
                }
                case 3: {
                    int eqpId = SCANNER.nextInt();
                    adventurers.get(advId).delValueBody(eqpId);
                    break;
                }
                case 4:
                    System.out.println(adventurers.get(advId).sumValue());
                    break;
                case 5:
                    System.out.println(adventurers.get(advId).findMaxValue());
                    break;
                case 6:
                    System.out.println(adventurers.get(advId).getValueBodies().size());
                    break;
                case 7: {
                    int eqpId = SCANNER.nextInt();
                    System.out.println(adventurers.get(advId).findValueBody(eqpId) + ".");
                    break;
                }
                case 8: {
                    Adventurer adv = adventurers.get(advId);
                    adv.valueUsedBy(adv);
                    break;
                }
                case 9:
                    System.out.println(adventurers.get(advId) + ".");
                    break;
                case 10:
                    Adventurer adv2 = adventurers.get(SCANNER.nextInt());
                    adventurers.get(advId).addValueBody(adv2);
                    break;
                default:
                    System.out.println("Unknown operation");
            }
        }
    }

    public static Equipment doOperationTwo(int eqpType) {
        int id = SCANNER.nextInt();
        String name = SCANNER.next();
        long price = SCANNER.nextLong();
        Equipment equipment = null;
        if (eqpType > 0 && eqpType <= 3) {
            double capacity = SCANNER.nextDouble();
            if (eqpType == 1) {
                equipment = new Bottle(id, name, price, capacity);
            } else if (eqpType == 2) {
                equipment = new HealingPotion(id, name, price, capacity, SCANNER.nextDouble());
            } else {
                equipment = new ExpBottle(id, name, price, capacity, SCANNER.nextDouble());
            }
        } else if (eqpType > 3 && eqpType <= 6) {
            double sharpness = SCANNER.nextDouble();
            if (eqpType == 4) {
                equipment = new Sword(id, name, price, sharpness);
            } else if (eqpType == 5) {
                equipment = new RareSword(id, name, price, sharpness, SCANNER.nextDouble());
            } else {
                equipment = new EpicSword(id, name, price, sharpness, SCANNER.nextDouble());
            }
        }
        return equipment;
    }
}
