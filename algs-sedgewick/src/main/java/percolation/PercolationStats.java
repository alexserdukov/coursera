package percolation;

import utils.In;
import utils.StdRandom;
import utils.StdStats;

/**
 * Created by alex on 08.02.15.
 */
public class PercolationStats {

    private double[] fractions;
    private int t;


    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int n, int t){
        if (n <= 0)
            throw new IllegalArgumentException("n cannot be less or equal to zero");

        if (t <= 0)
            throw new IllegalArgumentException("t cannot be less or equal to zero");

        this.t = t;

        fractions = new double[t];

        for (int i = 0; i < t; i++){
            Percolation percolation = new Percolation(n);
            int openSites = 0;
            while(!percolation.percolates()){
                int r1 = StdRandom.uniform(1,n+1);
                int r2 = StdRandom.uniform(1,n+1);
                if (!percolation.isOpen(r1,r2)){
                    percolation.open(r1,r2);
                    openSites++;
                }
            }
            fractions[i] = Double.valueOf(openSites) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(fractions);
    }
    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(fractions);

    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo()   {
        return mean() - 1.96 * stddev()/Math.sqrt(t);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + 1.96 * stddev()/Math.sqrt(t);
    }
    // test client (described below)
    public static void main(String[] args){
        PercolationStats stat = new PercolationStats(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        System.out.println("mean = " + stat.mean());
        System.out.println("stddev = " + stat.stddev());
        System.out.println("95% confidence interval = " + stat.confidenceLo() + ", " + stat.confidenceHi());
    }

}
