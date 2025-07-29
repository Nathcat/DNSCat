#include <dnscat/FileParsing.hpp>
#include <dnscat/RR.hpp>

int read_master_file(const char* path, RR** o) {
    std::ifstream file(path);

    int rr_count = 0;

    while (!file.eof()) {
        std::string name, type_name, ttl, value;

        name = __read_until_delim(&file);
        type_name = __read_until_delim(&file);
        ttl = __read_until_delim(&file);
        value = __read_until_delim(&file);

        RR* tmp = (RR*) malloc((++rr_count) * sizeof(RR));
        memcpy(tmp, *o, rr_count - 1);
        (tmp + rr_count - 1)->name = name;
        (tmp + rr_count - 1)->type = type_name_to_code(type_name);
        (tmp + rr_count - 1)->cls = RR_CLASS_IN;
        (tmp + rr_count - 1)->ttl = (unsigned int) std::stoi(ttl);
        (tmp + rr_count - 1)->data = value;

        *o = tmp;
    }

    return rr_count;
}

std::string __read_until_delim(std::ifstream* file) {
    std::string s = "";

    char c;
    file->read(&c, 1);
    while (c != MASTER_FILE_DELIM) {
        s.push_back(c);
        file->read(&c, 1);

        if (c == '\n') return s;
        else if (file->eof()) return s;
    }

    return s;
}