#include <algorithm>
#include "InlineSequentialConvolution.h"

void InlineSequentialConvolution::shiftCache(std::vector<std::vector<int>>& cache, int newLine) const {
    for (int i = 0; i < halfK; ++i) {
        cache[i] = cache[i + 1];
    }
    if (newLine < N) {
        cache[halfK] = matrix[newLine];
    }
}

int InlineSequentialConvolution::getPixelValue(const std::vector<std::vector<int>>& cache, int i, int j, int rowIndex) const {
    int row = std::max(0, std::min(i, N - 1));
    int col = std::max(0, std::min(j, M - 1));
    if (row > rowIndex) {       // inseamna ca linia row nu este in cache, luam direct din matrice
        return matrix[row][col];
    }
    return cache[row - rowIndex + halfK][col];
}

int InlineSequentialConvolution::convolvePixel(const std::vector<std::vector<int>>& cache, int i, int j) const {
    int sum = 0;
    for (int ki = 0; ki < K; ++ki) {
        for (int kj = 0; kj < K; ++kj) {
            int matrixRow = i + ki - halfK;
            int matrixCol = j + kj - halfK;
            sum += getPixelValue(cache, matrixRow, matrixCol, i) * kernel[ki][kj];
        }
    }
    return sum;
}

std::vector<std::vector<int>> InlineSequentialConvolution::convolve() {
    std::vector<std::vector<int>> cache(halfK + 1, std::vector<int>(M, 0));
    for (int i = 0; i < N; ++i) {
        shiftCache(cache, i);
        for (int j = 0; j < M; ++j) {
            matrix[i][j] = convolvePixel(cache, i, j);
        }
    }
    return matrix;
}
