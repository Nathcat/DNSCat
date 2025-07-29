#include <dnscat/RR.hpp>

const unsigned short type_name_to_code(std::string name) {
    if (name.compare("A") == 0) {
        return RR_TYPE_A;
    }
    else if (name.compare("NS") == 0) {
        return RR_TYPE_NS;
    }
    else if (name.compare("CNAME") == 0) {
        return RR_TYPE_CNAME;
    }
    else if (name.compare("SOA") == 0) {
        return RR_TYPE_SOA;
    }
    else if (name.compare("WKS") == 0) {
        return RR_TYPE_WKS;
    }
    else if (name.compare("PTR") == 0) {
        return RR_TYPE_PTR;
    }
    else if (name.compare("HINFO") == 0) {
        return RR_TYPE_HINFO;
    }
    else if (name.compare("MINFO") == 0) {
        return RR_TYPE_MINFO;
    }
    else if (name.compare("MX") == 0) {
        return RR_TYPE_MX;
    }
    else if (name.compare("TXT") == 0) {
        return RR_TYPE_TXT;
    }
    else if (name.compare("AXFR") == 0) {
        return RR_QTYPE_AXFR;
    }
    else if (name.compare("MAILB") == 0) {
        return RR_QTYPE_MAILB;
    }
    else if (name.compare("*") == 0) {
        return RR_QTYPE_ALL;
    }
    else {
        return 0;
    }
}

const unsigned short class_name_to_code(std::string name) {
    if (name.compare("IN") == 0) { 
        return RR_CLASS_IN;
    }
    else if (name.compare("*") == 0) {
        return RR_QCLASS_ANY;
    }
    else {
        return 0;
    }
}