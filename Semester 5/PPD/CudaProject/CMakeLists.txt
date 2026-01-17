cmake_minimum_required(VERSION 3.18)

set(CMAKE_CUDA_FLAGS "${CMAKE_CUDA_FLAGS} -allow-unsupported-compiler")

add_definitions(-D_ALLOW_COMPILER_AND_STL_VERSION_MISMATCH)
add_definitions(-D_ALLOW_RTCc_IN_STL)

set(CMAKE_CUDA_FLAGS "${CMAKE_CUDA_FLAGS} -Xcompiler /D_ALLOW_COMPILER_AND_STL_VERSION_MISMATCH")

project(CudaProject LANGUAGES CXX CUDA)

set(CMAKE_CUDA_ARCHITECTURES 61)

add_executable(CudaProject main.cu
        convolution/InlineCudaConvolution.cu
        convolution/InlineSequentialConvolution.h
        convolution/InlineSequentialConvolution.cpp
        utils/MatrixFileManager.h
        utils/MatrixValidator.cpp
        utils/MatrixFileManager.cpp
)