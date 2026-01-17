
#include "MatrixValidator.h"
#include <fstream>
#include <iostream>

bool MatrixValidator::validate(const std::string &validSeqPath, const std::string &parallelPath) {
    std::ifstream sequentialFile(validSeqPath);
    if (!sequentialFile) {
        throw std::runtime_error("Cannot open file: " + validSeqPath);
    }

    std::ifstream parallelFile(parallelPath);
    if (!parallelFile) {
        throw std::runtime_error("Cannot open file: " + parallelPath);
    }

    std::string seqLine, parLine;
    while (std::getline(sequentialFile, seqLine) && std::getline(parallelFile, parLine)) {
        if (seqLine != parLine) {
            std::cerr << "Validation failed: " << validSeqPath << " and " << parallelPath << " are not equal" << std::endl;
            return false;
        }
    }

    return true;
}
