package ru.madzi.lab.stat.jama;

import java.io.Serializable;

/**
 * QR разложение.
 */
public class QRDecomposition implements Serializable {

    private double[][] QR;

    private int m;

    private int n;

    private double[] Rdiag;

    public QRDecomposition(Matrix A) {
        QR = A.getArrayCopy();
        m = A.getRowDimension();
        n = A.getColumnDimension();
        Rdiag = new double[n];

        for (int k = 0; k < n; ++k) {
            double nrm = 0;
            for (int i = k; i < m; ++i) {
                nrm = Maths.hypot(nrm, QR[i][k]);
            }

            if (nrm != 0.0) {
                if (QR[k][k] < 0) {
                    nrm = -nrm;
                }
                for (int i = k; i < m; ++i) {
                    QR[i][k] /= nrm;
                }
                QR[k][k] += 1.0;

                for (int j = k + 1; j < n; ++j) {
                    double s = 0.0;
                    for (int i = k; i < m; ++i) {
                        s += QR[i][k] * QR[i][j];
                    }
                    s = -s/QR[k][k];
                    for (int i = k; i < m; ++i) {
                        QR[i][j] += s * QR[i][k];
                    }
                }
            }
            Rdiag[k] = -nrm;
        }
    }

    public boolean isFullRank() {
        for (int j = 0; j < n; ++j) {
            if (Rdiag[j] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает нижнетреугольную матрицу, содержащую собственные числа.
     *
     * @return нижнетреугольная матрица
     */
    public Matrix getH() {
        Matrix X = new Matrix(m, n);
        double[][] H = X.getArray();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                H[i][j] = (i >= j) ? QR[i][j] : 0.0;
            }
        }
        return X;
    }

    /**
     * Возвращает верхнетреугольную матрицу.
     *
     * @return верхнетреугольная матрица
     */
    public Matrix getR() {
        Matrix X = new Matrix(n, n);
        double[][] R = X.getArray();
        for (int i = 0; i < n; ++i) {
            for (int j =  0; j < n; ++j) {
                R[i][j] = (i == j) ? Rdiag[i] : (i < j) ? QR[i][j] : 0.0;
            }
        }
        return X;
    }

    public Matrix getQ() {
        Matrix X = new Matrix(m, n);
        double[][] Q = X.getArray();
        for (int k = n - 1; k >= 0; --k) {
            for (int i = 0; i < m; ++i) {
                Q[i][k] = 0.0;
            }
            Q[k][k] = 1.0;
            for (int j = k; j < n; ++j) {
                if (QR[k][k] != 0) {
                    double s = 0.0;
                    for (int i = k; i < m; ++i) {
                        s += QR[i][k] * Q[i][j];
                    }
                    s = -s/QR[k][k];
                    for (int i = k; i < m; ++i) {
                        Q[i][j] += s * QR[i][k];
                    }
                }
            }
        }
        return X;
    }

    public Matrix solve(Matrix B) {
        if (B.getRowDimension() != m) {
            throw new IllegalArgumentException("Matrix row dimesions must agree.");
        }
        if (!this.isFullRank()) {
            throw new RuntimeException("Matrix is rank deficient.");
        }

        int nx = B.getColumnDimension();
        double[][] X = B.getArrayCopy();

        for (int k = 0; k < n; ++k) {
            for (int j = 0; j < nx; ++j) {
                double s = 0.0;
                for (int i = k; i < m; ++i) {
                    s += QR[i][k] * X[i][j];
                }
                s = -s/QR[k][k];
                for (int i = k; i < m; ++i) {
                    X[i][j] += s * QR[i][k];
                }
            }
        }

        for (int k = n - 1; k >= 0; --k) {
            for (int j = 0; j < nx; ++j) {
                X[k][j] /= Rdiag[k];
            }
            for (int i = 0; i < k; ++i) {
                for (int j = 0; j < nx; ++j) {
                    X[i][j] -= X[k][j] * QR[i][k];
                }
            }
        }
        return (new Matrix(X, n, nx).getMatrix(0, n - 1, 0, nx - 1));
    }

}
