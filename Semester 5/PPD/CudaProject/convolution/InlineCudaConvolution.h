#ifndef INLINECUDACONVOLUTION_H
#define INLINECUDACONVOLUTION_H

#include "InlineCudaConvolution.h"
#include <vector>

#include "InlineConvolution.h"

class InlineCudaConvolution : public InlineConvolution {
public:
    InlineCudaConvolution(const std::vector<std::vector<int>>& matrix,
                          const std::vector<std::vector<int>>& kernel);

    std::vector<std::vector<int>> convolve() override;

    ~InlineCudaConvolution() override = default;
};

#endif //INLINECUDACONVOLUTION_H