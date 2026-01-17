
#ifndef INLINECONVOLUTION_H
#define INLINECONVOLUTION_H
#include <vector>

enum class SynchronizationType {
    BARRIER, FLAG
};

class InlineConvolution {
protected:
    int N, M, K, P, halfK;
    std::vector<std::vector<int>> matrix, kernel;

public:
    virtual ~InlineConvolution() = default;

    InlineConvolution(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int P)
    : P(P), matrix(matrix), kernel(kernel) {
        N = matrix.size();
        M = N > 0 ? matrix[0].size() : 0;
        K = kernel.size();
        halfK = K / 2;
    }

    virtual std::vector<std::vector<int>> convolve() = 0;
};

#endif //INLINECONVOLUTION_H
