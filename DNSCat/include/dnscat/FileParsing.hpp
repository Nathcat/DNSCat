#ifndef DNSCAT_FILEPARSING_HPP
#define DNSCAT_FILEPARSING_HPP

#include <dnscat/RR.hpp>
#include <dnscat/errors.hpp>
#include <fstream>
#include <stdlib.h>
#include <string.h>

#define MASTER_FILE_DELIM '\t'

/// @brief Read a domain master file
/// @param path The path to the file
/// @param o The output buffer for resource records
/// @return The number of resource records read from the file
int read_master_file(const char* path, RR** o);

std::string __read_until_delim(std::ifstream* file);

#endif