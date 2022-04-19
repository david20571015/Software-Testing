// Use-after-return

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int *foo() {
  int a[8] = {0};
  return a;
}

int main() {
  int a = *foo();  // use-after-return
  return 0;
}