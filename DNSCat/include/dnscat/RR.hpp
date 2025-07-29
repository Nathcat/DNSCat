#ifndef DNSCAT_RR_HPP
#define DNSCAT_RR_HPP

#include <iostream>

const unsigned short RR_TYPE_A = 1;
const unsigned short RR_TYPE_NS = 2;
const unsigned short RR_TYPE_CNAME = 5;
const unsigned short RR_TYPE_SOA = 6;
const unsigned short RR_TYPE_WKS = 11;
const unsigned short RR_TYPE_PTR = 12;
const unsigned short RR_TYPE_HINFO = 13;
const unsigned short RR_TYPE_MINFO = 14;
const unsigned short RR_TYPE_MX = 15;
const unsigned short RR_TYPE_TXT = 16;
const unsigned short RR_QTYPE_AXFR = 252;
const unsigned short RR_QTYPE_MAILB = 253;
const unsigned short RR_QTYPE_ALL = 255;

const unsigned short RR_CLASS_IN = 1;
const unsigned short RR_QCLASS_ANY = 255;

struct RR {
    std::string name;
    unsigned short type;
    unsigned short cls;
    unsigned int ttl;
    std::string data;
} typedef RR;

const unsigned short type_name_to_code(std::string name);
const unsigned short class_name_to_code(std::string name);

const unsigned int

#endif