cmake_minimum_required(VERSION 3.20)
project(MPI_Test)

set(CMAKE_CXX_STANDARD 17)

# 1. Find the MPI package
find_package(MPI REQUIRED)

add_executable(MPI_Test cerinta1.cpp
        cerinta2.cpp cerinta2_scatter.cpp
        cerinta3.cpp
        cerinta4.cpp
        cerinta5.cpp
        cerinta6.cpp
        cerinta7.cpp
        cerinta8.cpp)

# 2. Link against the modern MPI target
# This automatically handles Includes AND Libraries.
# You do NOT need manual include_directories or absolute paths.
target_link_libraries(MPI_Test PRIVATE MPI::MPI_CXX)

# 3. Handle MSVC exceptions (Fixes the C4530 warning)
if(MSVC)
    add_compile_options(/EHsc)
endif()

# (Optional) Verify output in the CMake console
message(STATUS "MPI executable: ${MPI_CXX_COMPILER}")
message(STATUS "MPI found: ${MPI_CXX_FOUND}")