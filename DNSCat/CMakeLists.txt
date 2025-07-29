project(DNSCat)

add_executable(DNSCat src/main.cpp)

target_include_directories(DNSCat PUBLIC include ../json/include)
target_sources(DNSCat PUBLIC src/FileParsing.cpp src/RR.cpp)
