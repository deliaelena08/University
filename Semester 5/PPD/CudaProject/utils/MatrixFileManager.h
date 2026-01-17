
#ifndef MATRIXFILEMANAGER_H
#define MATRIXFILEMANAGER_H
#include <string>
#include <vector>


class MatrixFileManager {
public:
    static void generateInputMatrix(int N, int M, const std::string& filePath, int pixelValueBound);

    static void generateInputKernel(int K, const std::string& filePath, int kernelValueBound);

    static std::pair<std::vector<std::vector<int>>, std::vector<std::vector<int>>> readInput(const std::string& filePath);

    static void writeOutput(const std::vector<std::vector<int>>& outputMatrix, const std::string& filePath);
};


#endif //MATRIXFILEMANAGER_H
