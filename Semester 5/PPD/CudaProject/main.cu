#include <iostream>
#include <string>
#include <chrono>
#include <fstream>

#include "utils/MatrixFileManager.h"
#include "convolution/InlineConvolution.h"
#include "convolution/InlineSequentialConvolution.h"
#include "convolution/InlineCudaConvolution.h"
#include "utils/MatrixValidator.h"

const std::string INPUT_DIR = "./inputs/";
const std::string OUTPUT_DIR = "./outputs/";

enum class ComputationMethod {
    INLINE_SEQUENTIAL,
    CUDA
};

int main(const int argc, char *argv[]) {
    // Example for CUDA: input_1000_1000_3.txt 1 CUDA
    if (argc < 2 || argc > 5) {
        std::cerr << "Usage: " << argv[0] << " <input_file> <method>" << std::endl;
        return 1;
    }

    try {
        const std::string inputFilePath = INPUT_DIR + std::string(argv[1]);
        const std::string methodStr = std::string(argv[2]);

        ComputationMethod method;
        if (methodStr == "INLINE_SEQUENTIAL") method = ComputationMethod::INLINE_SEQUENTIAL;
        else if (methodStr == "CUDA") method = ComputationMethod::CUDA;
        else throw std::runtime_error("Unknown method: " + methodStr);

        // Read Input
        std::vector<std::vector<int>> matrix, kernel;
        auto data = MatrixFileManager::readInput(inputFilePath);
        matrix = data.first;
        kernel = data.second;
        const int N = matrix.size();
        const int M = matrix[0].size();
        const int K = kernel.size();

        InlineConvolution *convolution = nullptr;
        std::string outputPath;

        // Select Implementation
        if (method == ComputationMethod::INLINE_SEQUENTIAL) {
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + ".txt";
            convolution = new InlineSequentialConvolution(matrix, kernel);
        }
        else if (method == ComputationMethod::CUDA) {
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + ".txt";
            convolution = new InlineCudaConvolution(matrix, kernel);
        }

        // Timing
        auto startTime = std::chrono::high_resolution_clock::now();
        auto outputMatrix = convolution->convolve();
        auto endTime = std::chrono::high_resolution_clock::now();

        MatrixFileManager::writeOutput(outputMatrix, outputPath);
        auto duration = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << duration << std::endl; // Output time for scripts to capture

        // Validation against Sequential
        std::string validSeqPath = OUTPUT_DIR + "output_INLINE_SEQUENTIAL_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + ".txt";

        // Simple check to see if validation file exists
        std::ifstream f(validSeqPath.c_str());
        if (f.good()) {
            if (!MatrixValidator::validate(validSeqPath, outputPath)) {
                std::cerr << "VALIDATION FAILED!" << std::endl;
            } else {
                std::cout << "Validation Passed." << std::endl;
            }
        } else {
             std::cout << "Validation skipped (Sequential output not found)." << std::endl;
        }

        if (convolution) delete convolution;

    } catch (const std::exception &e) {
        std::cerr << "Error: " << e.what() << std::endl;
        return 1;
    }
    return 0;
}