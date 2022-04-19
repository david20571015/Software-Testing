// Global out-of-bounds read/write

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int a[8] = {0};

int main() {
  a[8] = 0xabcd;  // out-of-bound write
  int b = a[8];   // out-of-bound read
  return 0;
}