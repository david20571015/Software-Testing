// Heap out-of-bound read/write

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
  int *a = (int *)malloc(8);
  a[8] = 0xabcd;  // out-of-bound write
  int b = a[8];   // out-of-bound read
  free(a);
  return 0;
}