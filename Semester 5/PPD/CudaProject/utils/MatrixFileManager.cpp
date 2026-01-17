
#include "MatrixFileManager.h"
#include <fstream>
#include <random>


void MatrixFileManager::generateInputMatrix(int N, int M, const std::string& filePath, int pixelValueBound) {
    std::ofstream outFile(filePath);
    if (!outFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    outFile << N << " " << M << std::endl;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dist(0, pixelValueBound);

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            outFile << dist(gen) << " ";
        }
        outFile << std::endl;
    }

    outFile.close();
}

void MatrixFileManager::generateInputKernel(int K, const std::string& filePath, int kernelValueBound) {
    std::ofstream outFile(filePath, std::ios_base::app); // append to the existing file
    if (!outFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    outFile << K << std::endl;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dist(0, kernelValueBound);

    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {
            outFile << dist(gen) << " ";
        }
        outFile << std::endl;
    }

    outFile.close();
}

std::pair<std::vector<std::vector<int> >, std::vector<std::vector<int> > > MatrixFileManager::readInput(const std::string &filePath) {
    std::ifstream inFile(filePath);
    if (!inFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    int N, M;
    inFile >> N >> M;

    std::vector<std::vector<int>> valuesMatrix(N, std::vector<int>(M));
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            int value;
            inFile >> value;
            valuesMatrix[i][j] = value;
        }
    }

    int K;
    inFile >> K;

    std::vector<std::vector<int>> kernelMatrix(K, std::vector<int>(K));
    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {
            int value;
            inFile >> value;
            kernelMatrix[i][j] = value;
        }
    }

    inFile.close();
    return {valuesMatrix, kernelMatrix};
}


void MatrixFileManager::writeOutput(const std::vector<std::vector<int>>& outputMatrix, const std::string& filePath) {
    std::ofstream outFile(filePath);
    if (!outFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    for (int i = 0; i < outputMatrix.size(); ++i) {
        for (int j = 0; j < outputMatrix[0].size(); ++j) {
            outFile << outputMatrix[i][j] << " ";
        }
        outFile << std::endl;
    }

    outFile.close();
}