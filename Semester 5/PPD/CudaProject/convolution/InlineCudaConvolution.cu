#include "InlineCudaConvolution.h"
#include <cuda_runtime.h>
#include <iostream>
#include <algorithm>

/**
 * CUDA KERNEL: Runs on the GPU
 * Each thread calculates exactly ONE pixel.
 */
__global__ void convolutionChunkKernel(const int *d_input, int *d_output, const int *d_kernel,
                                       int M, int K, int halfK,
                                       int chunkH, int globalStartRow, int totalN) {

    // Calculate which pixel this specific thread is responsible for
    int col = blockIdx.x * blockDim.x + threadIdx.x;
    int localRow = blockIdx.y * blockDim.y + threadIdx.y;

    // Boundary check: ensure the thread is within the current chunk and matrix width
    if (localRow < chunkH && col < M) {
        int globalRow = globalStartRow + localRow;
        int sum = 0;

        // Loop through the KxK kernel
        for (int ki = 0; ki < K; ++ki) {
            for (int kj = 0; kj < K; ++kj) {
                // Find the neighbor's position in the global image
                int globalNeighborRow = globalRow + ki - halfK;
                int globalNeighborCol = col + kj - halfK;

                // If neighbor is outside the image, use the edge pixel
                int clampedGlobalRow = min(max(globalNeighborRow, 0), totalN - 1);
                int clampedCol = min(max(globalNeighborCol, 0), M - 1);

                // Since d_input only contains a small "strip" of the image, we must
                // translate the global row index to a local index within our buffer.
                int inputBufferOffset = max(globalStartRow - halfK, 0);
                int localInputRow = clampedGlobalRow - inputBufferOffset;

                // Multiply the pixel value by the kernel weight and add to sum
                int pixelVal = d_input[localInputRow * M + clampedCol];
                int kernelVal = d_kernel[ki * K + kj];
                sum += pixelVal * kernelVal;
            }
        }
        // Store the final calculated pixel in the output buffer
        d_output[localRow * M + col] = sum;
    }
}

InlineCudaConvolution::InlineCudaConvolution(const std::vector<std::vector<int>>& matrix,
                                             const std::vector<std::vector<int>>& kernel)
    : InlineConvolution(matrix, kernel, 1) {
}

std::vector<std::vector<int>> InlineCudaConvolution::convolve() {
    // We process the image in strips of 256 rows to manage GPU memory
    int rowsPerChunk = 256;

    // Flatten the 2D kernel vector into a 1D array for the GPU
    int* h_flatKernel = new int[K * K];
    for(int i=0; i<K; ++i)
        for(int j=0; j<K; ++j)
            h_flatKernel[i*K + j] = kernel[i][j];

    // Allocate and copy kernel to GPU memory
    int *d_kernel;
    cudaMalloc((void**)&d_kernel, K * K * sizeof(int));
    cudaMemcpy(d_kernel, h_flatKernel, K * K * sizeof(int), cudaMemcpyHostToDevice);

    // maxInputRows includes the chunk size + the "halo" (neighboring rows needed for calculation)
    int maxInputRows = rowsPerChunk + 2 * halfK;
    int* d_chunkInput;
    int* d_chunkOutput;

    // GPU Memory
    cudaMalloc((void**)&d_chunkInput, maxInputRows * M * sizeof(int));
    cudaMalloc((void**)&d_chunkOutput, rowsPerChunk * M * sizeof(int));

    // CPU temporary buffers
    int* h_tempInput = new int[maxInputRows * M];
    int* h_tempOutput = new int[rowsPerChunk * M];

    // Because we modify the matrix in-place, we must save "clean" copies of rows
    // that are about to be overwritten but will be needed by the NEXT chunk.
    int* seamBuffer = new int[halfK * M];

    for (int startRow = 0; startRow < N; startRow += rowsPerChunk) {
        int endRow = std::min(startRow + rowsPerChunk, N);
        int currentChunkHeight = endRow - startRow;

        // Calculate the range of rows we need to load (including the halo)
        int loadStart = std::max(0, startRow - halfK);
        int loadEnd = std::min(N, endRow + halfK);
        int inputHeight = loadEnd - loadStart;

        // A. Copy rows from the matrix to our temporary CPU buffer
        int inputIdx = 0;
        for (int i = loadStart; i < loadEnd; ++i) {
            for (int j = 0; j < M; ++j) {
                h_tempInput[inputIdx++] = matrix[i][j];
            }
        }

        // If this isn't the first chunk, the top rows in h_tempInput were modified
        // by the previous loop. We overwrite them with the clean values from seamBuffer.
        if (startRow > 0) {
            memcpy(h_tempInput, seamBuffer, halfK * M * sizeof(int));
        }

        // Before we process this chunk, save the original values of its bottom edge.
        // The next chunk will use these to restore its top halo.
        if (endRow < N) {
            int offsetInLocal = (endRow - halfK - loadStart) * M;
            memcpy(seamBuffer, &h_tempInput[offsetInLocal], halfK * M * sizeof(int));
        }

        // Copy the prepared (clean) strip to the GPU
        cudaMemcpy(d_chunkInput, h_tempInput, inputHeight * M * sizeof(int), cudaMemcpyHostToDevice);

        // Define thread blocks (16x16) and grid size
        dim3 blockSize(16, 16);
        dim3 gridSize((M + blockSize.x - 1) / blockSize.x,
                      (currentChunkHeight + blockSize.y - 1) / blockSize.y);

        // Launch the kernel
        convolutionChunkKernel<<<gridSize, blockSize>>>(
            d_chunkInput, d_chunkOutput, d_kernel,
            M, K, halfK,
            currentChunkHeight, startRow, N
        );

        // Ensure GPU is finished before proceeding
        cudaDeviceSynchronize();

        // Copy result from GPU to CPU
        cudaMemcpy(h_tempOutput, d_chunkOutput, currentChunkHeight * M * sizeof(int), cudaMemcpyDeviceToHost);

        // Overwrite the original matrix with the new convolved values
        int outputIdx = 0;
        for (int i = startRow; i < endRow; ++i) {
            for (int j = 0; j < M; ++j) {
                matrix[i][j] = h_tempOutput[outputIdx++];
            }
        }
    }


    // Free all allocated memory on GPU and CPU
    cudaFree(d_kernel);
    cudaFree(d_chunkInput);
    cudaFree(d_chunkOutput);
    delete[] h_flatKernel;
    delete[] h_tempInput;
    delete[] h_tempOutput;
    delete[] seamBuffer;

    return matrix;
}