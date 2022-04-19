// Use-after-free

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
  int *a = (int *)malloc(4);
  free(a);
  int b = a[0];  // use-after-free
  return 0;
}