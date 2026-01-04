cmake_minimum_required(VERSION 3.20)
project(Test_lab11)

set(CMAKE_CXX_STANDARD 17)

# 1. Find the MPI package
find_package(MPI REQUIRED)

add_executable(Test_lab11 main.cpp)

# 2. Link against the modern MPI target
# This automatically handles Includes AND Libraries.
# You do NOT need manual include_directories or absolute paths.
target_link_libraries(Test_lab11 PRIVATE MPI::MPI_CXX)

# 3. Handle MSVC exceptions (Fixes the C4530 warning)
if(MSVC)
    add_compile_options(/EHsc)
endif()

# (Optional) Verify output in the CMake console
message(STATUS "MPI executable: ${MPI_CXX_COMPILER}")
message(STATUS "MPI found: ${MPI_CXX_FOUND}")