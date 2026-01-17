#ifndef INLINESEQUENTIALCONVOLUTION_H
#define INLINESEQUENTIALCONVOLUTION_H
#include "InlineConvolution.h"


class InlineSequentialConvolution : public InlineConvolution {
public:
    InlineSequentialConvolution(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel)
        : InlineConvolution(matrix, kernel, 1) {}

    void shiftCache(std::vector<std::vector<int>>& cache, int newLine) const;
    int getPixelValue(const std::vector<std::vector<int>>& cache, int i, int j, int rowIndex) const;
    int convolvePixel(const std::vector<std::vector<int>>& cache, int i, int j) const;
    std::vector<std::vector<int>> convolve() override;

};


#endif //INLINESEQUENTIALCONVOLUTION_H
