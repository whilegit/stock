/*
 * SecssUtil.h
 *
 *  Created on: Jul 30, 2015
 *      Author: lynnlin
 */

#ifndef SECSSUTIL_H_
#define SECSSUTIL_H_

#include <stdbool.h>
#include <string.h>
#include <openssl/err.h>
#include <openssl/pem.h>
#include <openssl/pkcs12.h>

typedef struct key_value_pair_t {
	char* key;
	char* value;
} KEY_VALUE_PAIR;

char* get_certificate_Id(const char* filename, const char* password);

bool sign(const char* filename, const char* password,
		const unsigned char* unSignData, size_t uslen, unsigned char* signData,
		size_t* slen);

bool verify(const char* filename, const unsigned char* unSignData, size_t uslen,
		const unsigned char* signData, size_t slen);

bool encrypt(const char* filename, unsigned char* from, size_t flen,
		unsigned char* to, size_t* tlen);

bool decrypt(const char* filename, const char* password, unsigned char* from,
		size_t flen, unsigned char* to, size_t* tlen);

bool build_data(KEY_VALUE_PAIR* from, size_t flen, char* to, size_t* tlen);

#endif /* SECSSUTIL_H_ */
