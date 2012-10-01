import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        for (int k = 1; scan.hasNext(); k++) {
            int n = scan.nextInt();
            double[] coef = new double[n + 1];
            for (int i = 0; i <= n; i++)
                coef[i] = scan.nextDouble();

            double xlow = scan.nextDouble();
            double xhigh = scan.nextDouble();
            int inc = scan.nextInt();

            Polynomial p = new Polynomial(coef);
            p = p.square().multiply(Math.PI);
            double totalVolume = p.integrate(xlow, xhigh);

            System.out.printf("Case %d: %.2f\n", k, totalVolume);
            if (totalVolume < inc) {
                System.out.println("insufficient volume");
                continue;
            }

            double guess;
            double tickVolume;
            double tickMax = xhigh;
            double tickMin = xlow;
            double lastGuess = xlow;

            String ticks = "";
            for (int i = 0; i < 8 && p.integrate(lastGuess, xhigh) > inc; i++) {
                do {
                    guess = (tickMax + tickMin)/2;
                    tickVolume = p.integrate(lastGuess, guess);
                    if (tickVolume < inc)
                        tickMin = guess;
                    else
                        tickMax = guess;
                } while (Math.abs(tickVolume - inc) > 0.001);

                ticks += String.format("%.2f ", guess - xlow);
                lastGuess = guess;
                tickMin = guess;
                tickMax = xhigh;
            }
            System.out.println(ticks.trim());
        }
    }
}

class Polynomial {
    double[] coef;

    // a0 + a1x + a2x^2 + ...
    public Polynomial(double[] coef) {
        this.coef = coef;
    }

    public double integrate(double a, double b) {
        Polynomial integ = integral();
        return integ.evaluate(b) - integ.evaluate(a);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = coef.length - 1; i >= 0; i--)
            result = coef[i] + x*result;
        return result;
    }

    public Polynomial integral() {
        double[] newCoef = new double[coef.length + 1];
        newCoef[0] = 0;
        for (int i = 1; i < newCoef.length; i++)
            newCoef[i] = coef[i-1]/i;
        return new Polynomial(newCoef);
    }

    public Polynomial square() {
        double[] newCoef = new double[coef.length * 2 - 1];
        for (int i = 0; i < newCoef.length; i++)
            for (int j = 0; j <= i; j++)
                if (j < coef.length && i - j < coef.length)
                    newCoef[i] += coef[j] * coef[i-j];

        return new Polynomial(newCoef);
    }

    public Polynomial multiply(double c) {
        double[] newCoef = new double[coef.length];
        for (int i = 0; i < newCoef.length; i++)
            newCoef[i] = coef[i] * c;
        return new Polynomial(newCoef);
    }
}
