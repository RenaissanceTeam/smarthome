#include "Print.h"

class PrintLengthCounter: public Print {

public:

virtual size_t write(uint8_t);
virtual void reset();
virtual int len();
};
