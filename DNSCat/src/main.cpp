#include <iostream>
#include <dnscat/FileParsing.hpp>
#include <dnscat/RR.hpp>
#include <stdlib.h>

int main() {
    RR* rr;
    int rr_n = read_master_file("test.txt", &rr);

    std::cout << "Done" << std::endl;
}