package ru.madzi.lab.stat.jama;

import java.io.Serializable;

/**
 * LU разложение.
 */
public class LUDecomposition implements Serializable {

    private double[][] LU;

    private int m;

    private int n;

    private int pivsign;

    private int[] piv;

    public LUDecomposition(Matrix A) {
        LU = A.getArrayCopy();
        m = A.getRowDimension();
        n = A.getColumnDimension();
        piv = new int[m];
        for (int i = 0; i < m; ++i) {
            piv[i] = i;
        }
        pivsign = 1;
        double[] LUrowi;
        double[] LUcolj = new double[m];

        for (int j = 0; j < n; ++j) {
            for (int i = 0; i < m; ++i) {
                LUcolj[i] = LU[i][j];
            }

            for (int i = 0; i < m; ++i) {
                LUrowi = LU[i];

                int kmax = Math.min(i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; ++k) {
                    s += LUrowi[k] * LUcolj[k];
                }
                LUrowi[j] = LUcolj[i] -= s;
            }

            int p = j;
            for (int i = j + 1; i < m; ++i) {
                if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
                    p = i;
                }
            }
            if (p != j) {
                for (int k = 0; k < n; ++k) {
                    double t = LU[p][k];
                    LU[p][k] = LU[j][k];
                    LU[j][k] =  t;
                }
                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            if (j < m & LU[j][j] != 0.0) {
                for (int i = j + 1; i < m; ++i) {
                    LU[i][j] /= LU[j][j];
                }
            }
        }
    }

    public boolean isNonsingular() {
        for (int j = 0; j < n; ++j) {
            if (LU[j][j] == 0) {
                return false;
            }
        }
        return true;
    }

    public Matrix getL() {
        Matrix X = new Matrix(m, n);
        double[][] L = X.getArray();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                L[i][j] = (i == j) ? 1.0 : (i > j) ? LU[i][j] : 0.0;
            }
        }
        return X;
    }

    public Matrix getU() {
        Matrix X = new Matrix(n, n);
        double[][] U = X.getArray();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                U[i][j] = (i <= j) ? LU[i][j] : 0.0;
            }
        }
        return X;
    }

    public int[] getPivot() {
        int[] p = new int[m];
        for (int i = 0; i < m; ++i) {
            p[i] = piv[i];
        }
        return p;
    }

    public double[] getDoublePivot() {
        double[] vals = new double[m];
        for (int i = 0; i < m; ++i) {
            vals[i] = (double) piv[i];
        }
        return vals;
    }

    public double det() {
        if (m != n) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        double d = (double) pivsign;
        for (int j = 0; j < n; ++j) {
            d *= LU[j][j];
        }
        return d;
    }

    public Matrix solve (Matrix B) {
        if (B.getRowDimension() != m) {
            throw new IllegalArgumentException("Matrix row dimensions must be agree.");
        }
        if (!this.isNonsingular()) {
            throw new RuntimeException("Matrix is singular.");
        }

        int nx = B.getColumnDimension();
        Matrix Xmat = B.getMatrix(piv, 0, nx-1);
        double[][] X = Xmat.getArray();

        for (int k = 0; k < n; ++k) {
            for (int i = k + 1; i < n; ++i) {
                for (int j = 0; j < nx; ++j) {
                    X[i][j] -= X[k][j] * LU[i][k];
                }
            }
        }

        for (int k = n - 1; k >= 0; --k) {
            for (int j = 0; j < nx; ++j) {
                X[k][j] /= LU[k][k];
            }
            for (int i = 0; i < k; ++i) {
                for (int j = 0; j < nx; ++j) {
                    X[i][j] -= X[k][j] * LU[i][k];
                }
            }
        }
        return Xmat;
    }

}
