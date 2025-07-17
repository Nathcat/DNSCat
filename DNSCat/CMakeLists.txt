project(DNSCat)

add_executable(DNSCat src/main.cpp)

target_include_directories(DNSCat PUBLIC ../json/include)