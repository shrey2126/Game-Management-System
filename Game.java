import java.util.*;
import java.awt.*;
import javax.swing.*;

class Game {
    int id;
    String title;
    String type;
    int price;
    boolean availability;

    Game(int id, String title, String type, int price, boolean availability) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.price = price;
        this.availability = availability;
    }

    Scanner sc = new Scanner(System.in);

    public void payment() {
        System.out.println("Press 1 for Cash Payment");
        System.out.println("Press 2 for Debit Card Payment");
        System.out.println("Press 3 for UPI Payment");
        System.out.println("Enter your Payment Type: ");
        int ch = sc.nextInt();
        int flag = 0;

        switch (ch) {
            // for cash payment enter valid value as per total amount
            case 1: {
                while (true) {
                    System.out.println("Please Do Payment of Rs." + price);
                    double cash = sc.nextDouble();

                    if (cash >= 0 && cash <= price) {
                        flag = 1;
                        break;
                    } else {
                        System.out.println("Kindly pay " + price);
                    }
                }
                break;
            }

            // Debit Card Payment that takes only 12 digit debit card number & cvv & if both
            // are right as per condition, it sends otp & make payment successfull
            case 2: {
                while (true) {
                    int flag2 = 0;
                    System.out.println("For Payment of Rs." + price);
                    System.out.println("Enter your 12 Digit Debit Card Number");
                    String card = sc.next();

                    if (card.length() == 12) {
                        for (int i = 0; i < card.length(); i++) {
                            if (card.charAt(i) >= '0' && card.charAt(i) <= '9') {
                                flag2 = 1;
                                continue;
                            } else {
                                System.out.println("Kindly Enter Digit of Debit Card Number between 0 to 9");
                                System.out.println("& Kindly pay " + price);
                                flag2 = 0;
                                break;
                            }
                        }
                    } else {
                        System.out.println("Kindly Enter 12 Digit Debit Card Number");
                        System.out.println("& Kindly pay " + price);
                        continue;
                    }
                    if (flag2 == 1) {
                        System.out.println("Enter 3 Digit CVV: ");
                        String cvv = sc.next();

                        if (cvv.length() == 3) {
                            for (int i = 0; i < cvv.length(); i++) {
                                if (card.charAt(i) >= '0' && card.charAt(i) <= '9') {
                                    flag2 = 1;
                                    continue;
                                } else {
                                    System.out.println("Kindly Enter Digit of CVV between 0 to 9");
                                    System.out.println("& Kindly pay " + price);
                                    flag2 = 0;
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Kindly Enter 3 Digit CVV back on your Debit Card");
                            System.out.println("& Kindly pay " + price);
                            flag2 = 0;
                            continue;
                        }
                    }

                    if (flag2 == 1) {
                        System.out.println("Payable Amount is: " + price);
                        get_OTP();
                        System.out.println("Enter OTP: ");
                        int otp_card = sc.nextInt();

                        if (otp_card == a) {
                            flag = 1;
                            System.out.println("Payment Received Successfully of Rs." + price);
                            break;
                        } else {
                            System.out.println("Recieved OTP did'n match");
                            System.out.println("Payment Failed!");
                            continue;
                        }
                    }

                }
                break;
            }

            // UPI Payment using upi pin of 4 digits only & payment done
            case 3: {
                // UPI Payment
                System.out.println("Payable Amount is: " + price);
                while (true) {
                    System.out.println("Enter UPI Pin: ");
                    String pass = sc.next();
                    System.out.println(pass);
                    if (pass.length() == 4) {
                        flag = 1;
                        break;
                    } else {
                        System.out.println("Enter 4 digit UPI Pin");
                    }
                }

                break;
            }
            default: {
                System.out.println("Kindally Enter Valid Option");
            }
        }

        if (flag == 1) {
            System.out.println("We Recieved Your Payment of " + price);
            System.out.println(
                    "-------------------------------------Thank You-------------------------------------------");
        }

    }

    static int a = 0;

    // Sends otp to other window using java swing concept
    public void get_OTP() {
        JFrame OTP = new JFrame();
        OTP.setBounds(500, 350, 200, 200);
        OTP.setTitle("OTP-One Time Password");
        // OTP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = OTP.getContentPane();
        a = (int) (Math.random() * 10000);
        int ans = countdigi(a);
        if (ans == 3) {
            a *= 10;
        }
        if (ans == 2) {
            a *= 100;
        }
        if (ans == 1) {
            a *= 1000;
        }
        if (ans == 0) {
            a *= 10000;
        }
        JLabel otp = new JLabel();
        otp.setText("OTP: " + a);
        c.add(otp);
        OTP.setVisible(true);
    }

    public int countdigi(int n) {
        int count = 0;
        while (n > 0) {
            count++;
            n /= 10;
        }
        return count;
    }
}