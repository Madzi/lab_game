package ru.madzi.lab.stat.jama;

import static java.lang.Math.*;

/**
 * Сингулярное раложение матрицы.
 */
public class SingularValueDecomposition {

    private double[][] U;

    private double[][] V;

    private double[] s;

    private int m;

    private int n;

    public SingularValueDecomposition(Matrix Arg) {
        double[][] A = Arg.getArrayCopy();
        m = Arg.getRowDimension();
        n = Arg.getColumnDimension();

        int nu = min(m, n);
        s = new double[min(m + 1, n)];
        U = new double[m][nu];
        V = new double[n][n];
        double[] e = new double[n];
        double[] work = new double[m];
        boolean wantu = true;
        boolean wantv = true;

        int nct = min(m - 1, n);
        int nrt = max(0, min(n - 2, m));
        for (int k = 0; k < max(nct, nrt); ++k) {
            if (k < nct) {
                s[k] = 0;
                for (int i = k; i < m; ++i) {
                    s[k] = Maths.hypot(s[k], A[i][k]);
                }
                if (s[k] != 0.0) {
                    if (A[k][k] < 0.0) {
                        s[k] = -s[k];
                    }
                    for (int i = k; i < m; ++i) {
                        A[i][k] /= s[k];
                    }
                    A[k][k] += 1.0;
                }
                s[k] = -s[k];
            }

            for (int j = k + 1; j < n; ++j) {
                if ((k < nct) & (s[k] != 0.0)) {
                    double t = 0;
                    for (int i = k; i < m; ++i) {
                        t += A[i][k] * A[i][j];
                    }
                    t = -t / A[k][k];
                    for (int i = k; i < m; ++i) {
                        A[i][j] += t * A[i][k];
                    }
                }
                e[j] = A[k][j];
            }
            if (wantu & (k < nct)) {
                for (int i = k; k < m; ++i) {
                    U[i][k] = A[i][k];
                }
            }
            if (k < nrt) {
                e[k] = 0;
                for (int i = k + 1; i < n; ++i) {
                    e[k] = Maths.hypot(e[k], e[i]);
                }
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0) {
                        e[k] = -e[k];
                    }
                    for (int i = k + 1; i < n; ++i) {
                        e[i] /= e[k];
                    }
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if ((k + 1 < m) & (e[k] != 0.0)) {
                    for (int i = k + 1; i < m; ++i) {
                        work[i] = 0.0;
                    }
                    for (int j = k + 1; j < n; ++j) {
                        for (int i = k + 1; i < m; ++i) {
                            work[i] += e[j] * A[i][j];
                        }
                    }
                    for (int j = k + 1; j < n; ++j) {
                        double t = -e[j] / e[k + 1];
                        for (int i = k + 1; i < m; ++i) {
                            A[i][j] += t * work[i];
                        }
                    }
                }
                if (wantv) {
                    for (int i = k + 1; i < n; ++i) {
                        V[i][k] = e[i];
                    }
                }
            }
        }
        int p = min(n, m + 1);
        if (nct < n) {
            s[nct] = A[nct][nct];
        }
        if (m < p) {
            s[p - 1] = 0.0;
        }
        if (nrt + 1 < p) {
            e[nrt] = A[nrt][p - 1];
        }
        e[p - 1] = 0.0;

        if (wantu) {
            for (int j = nct; j < nu; ++j) {
                for (int i = 0; i < m; ++i) {
                    U[i][j] = 0.0;
                }
                U[j][j] = 1.0;
            }
            for (int k = nct - 1; k >= 0; --k) {
                if (s[k] != 0.0) {
                    for (int j = k + 1; j < nu; ++j) {
                        double t = 0;
                        for (int i = k; i < m; ++i) {
                            t += U[i][k] * U[i][j];
                        }
                        t = -t / U[k][k];
                        for (int i = k; i < m; ++i) {
                            U[i][j] += t * U[i][k];
                        }
                    }
                    for (int i = k; i < m; ++i) {
                        U[i][k] = -U[i][k];
                    }
                    U[k][k] = 1.0 + U[k][k];
                    for (int i = 0; i < k - 1; ++i) {
                        U[i][k] = 0.0;
                    }
                } else {
                    for (int i = 0; i < m; ++i) {
                        U[i][k] = 0.0;
                    }
                    U[k][k] = 1.0;
                }
            }
        }

        if (wantv) {
            for (int k = n - 1; k >= 0; --k) {
                if ((k < nrt) & (e[k] != 0.0)) {
                    for (int j = k + 1; j < nu; ++j) {
                        double t = 0;
                        for (int i = k + 1; i < n; ++i) {
                            t += V[i][k] * V[i][j];
                        }
                        t = -t / V[k + 1][k];
                        for (int i = k + 1; i < n; ++i) {
                            V[i][j] += t * V[i][k];
                        }
                    }
                }
                for (int i = 0; i < n; ++i) {
                    V[i][k] = 0.0;
                }
                V[k][k] = 1.0;
            }
        }

        int pp = p - 1;
        int iter = 0;
        double eps = pow(2.0, -52.0);
        double tiny = pow(2.0, -966.0);
        while (p > 0) {
            int k;
            int kase;
            for (k = p - 2; k >= -1; --k) {
                if (k == -1) {
                    break;
                }
                if (abs(e[k]) <= tiny + eps * (abs(s[k]) + abs(s[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                kase = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; --ks) {
                    if (ks == k) {
                        break;
                    }
                    double t = (ks != p ? abs(e[ks]) : 0.0) + (ks != k + 1 ? abs(e[ks - 1]) : 0.0);
                    if (abs(s[ks]) <= tiny + eps * t) {
                        s[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    kase = 3;
                } else if (ks == p - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k = ks;
                }
            }
            ++k;

            switch (kase) {
                case 1: {
                    double f = e[p - 2];
                    e[p - 2] = 0.0;
                    for (int j = p - 2; j >= k; --j) {
                        double t = Maths.hypot(s[j], f);
                        double cs = s[j] / t;
                        double sn = f / t;
                        s[j] = t;
                        if (j != k) {
                            f = -sn * e[j - 1];
                            e[j - 1] = cs * e[j - 1];
                        }
                        if (wantv) {
                            for (int i = 0; i < n; ++i) {
                                t = cs * V[i][j] + sn * V[i][p - 1];
                                V[i][p - 1] = -sn * V[i][j] + cs * V[i][p - 1];
                                V[i][j] = t;
                            }
                        }
                    }
                }
                break;

                case 2: {
                    double f = e[k - 1];
                    e[k - 1] = 0.0;
                    for (int j = k; j < p; ++j) {
                        double t = Maths.hypot(s[j], f);
                        double cs = s[j] / t;
                        double sn = f / t;
                        s[j] = t;
                        f = -sn * e[j];
                        e[j] = cs * e[j];
                        if (wantu) {
                            for (int i = 0; i < m; ++i) {
                                t = cs * U[i][j] + sn * U[i][k - 1];
                                U[i][k - 1] = -sn * U[i][j] + cs * U[i][k - 1];
                                U[i][j] = t;
                            }
                        }
                    }
                }
                break;

                case 3: {
                    double scale = max(max(max(max(abs(s[p - 1]), abs(s[p - 2])), abs(e[p - 2])), abs(s[k])), abs(e[k]));
                    double sp = s[p - 1] / scale;
                    double spml = s[p - 2] / scale;
                    double epml = e[p - 2] / scale;
                    double sk = s[k] / scale;
                    double ek = e[k] / scale;
                    double b = ((spml + sp) * (spml - sp) + epml * epml) / 2.0;
                    double c = (sp * epml) * (sp * epml);
                    double shift = 0.0;
                    if ((b != 0.0) | (c != 0.0)) {
                        shift = sqrt(b * b + c);
                        if (b < 0.0) {
                            shift = -shift;
                        }
                        shift = c / (b + shift);
                    }
                    double f = (sk + sp) * (sk - sp) + shift;
                    double g = sk * ek;

                    for (int j = k; j < p - 1; ++j) {
                        double t = Maths.hypot(f, g);
                        double cs = f / t;
                        double sn = g / t;
                        if (j != k) {
                            e[j - 1] = t;
                        }
                        f = cs * s[j] + sn * e[j];
                        e[j] = cs * e[j] - sn * s[j];
                        g = sn * s[j + 1];
                        if (wantv) {
                            for (int i = 0; i < n; ++i) {
                                t = cs * V[i][j] + sn * V[i][j + 1];
                                V[i][j + 1] = -sn * V[i][j] + cs * V[i][j + 1];
                                V[i][j] = t;
                            }
                        }
                        t = Maths.hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        s[j] = t;
                        f = cs * e[j] + sn * s[j + 1];
                        s[j + 1] = -sn * e[j] + cs * s[j + 1];
                        g = sn * e[j + 1];
                        e[j + 1] = cs * e[j + 1];
                        if (wantu && (j < m - 1)) {
                            for (int i = 0; i < m; ++i) {
                                t = cs * U[i][j] + sn * U[i][j + 1];
                                U[i][j + 1] = -sn * U[i][j] + cs * U[i][j + 1];
                                U[i][j] = t;
                            }
                        }
                    }
                    e[p - 2] = f;
                    iter++;
                }
                break;

                case 4: {
                    if (s[k] <= 0.0) {
                        s[k] = (s[k] < 0.0 ? -s[k] : 0.0);
                        if (wantv) {
                            for (int i = 0; i <= pp; ++i) {
                                V[i][k] = -V[i][k];
                            }
                        }
                    }
                    while (k < pp) {
                        if (s[k] >= s[k + 1]) {
                            break;
                        }
                        double t = s[k];
                        s[k] = s[k + 1];
                        s[k + 1] = t;
                        if (wantv && (k < n - 1)) {
                            for (int i = 0; i < n; ++i) {
                                t = V[i][k + 1];
                                V[i][k + 1] = V[i][k];
                                V[i][k] = t;
                            }
                        }
                        ++k;
                    }
                    iter = 0;
                    --p;
                }
            }
        }
    }

    public Matrix getU() {
        return new Matrix(U, m, min(m + 1, n));
    }

    public Matrix getV() {
        return new Matrix(V, n, n);
    }

    public double[] getSingularValues() {
        return s;
    }

    public Matrix getS() {
        Matrix X = new Matrix(n, n);
        double[][] S = X.getArray();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                S[i][j] = 0.0;
            }
            S[i][i] = s[i];
        }
        return X;
    }

    public double norm2() {
        return s[0];
    }

    public double cond() {
        return s[0] / s[min(m, n) - 1];
    }

    public int rank() {
        double eps = pow(2.0, - 52.0);
        double tol = max(m, n) * s[0] * eps;
        int r = 0;
        for (int i = 0; i < s.length; ++i) {
            if (s[i] > tol) {
                r++;
            }
        }
        return r;
    }

}
