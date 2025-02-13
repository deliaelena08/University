cmake_minimum_required(VERSION 3.26)
project(SDA_tabeladisp)

set(CMAKE_CXX_STANDARD 17)

add_executable(SDA_tabeladisp main.cpp
        MDO.cpp
        MDO.h
        IteratorMDO.cpp
        IteratorMDO.h
        TestScurt.cpp
        TestScurt.h
        TestExtins.cpp
        TestExtins.h)
