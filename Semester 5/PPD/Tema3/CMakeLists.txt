cmake_minimum_required(VERSION 3.20)
project(Tema3)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_GENERATOR_PLATFORM x64)

find_package(MPI REQUIRED)
include_directories(SYSTEM ${MPI_INCLUDE_PATH})

add_executable(Tema3
        sequential.cpp
        standard.cpp
        standard_optimizat.cpp
        scatter_gather.cpp
        generate_data.cpp
)

target_link_libraries(Tema3 PRIVATE ${MPI_LIBRARIES})

message(STATUS "MPI Include path: ${MPI_INCLUDE_PATH}")
message(STATUS "MPI Libraries: ${MPI_LIBRARIES}")
