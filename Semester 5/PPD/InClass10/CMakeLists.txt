cmake_minimum_required(VERSION 4.0)
project(InClass10)

set(CMAKE_CXX_STANDARD 20)

add_executable(InClass10 main.cpp)
find_package(OpenMP REQUIRED)
target_link_libraries(InClass10 PRIVATE OpenMP::OpenMP_CXX)