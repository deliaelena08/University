cmake_minimum_required(VERSION 3.26)
project(sda_ArboreBianr)

set(CMAKE_CXX_STANDARD 17)

add_executable(sda_ArboreBianr main.cpp
        Matrice.cpp
        TestExtins.cpp
        TestScurt.cpp
        Iterator.h
        Iterator.cpp)
