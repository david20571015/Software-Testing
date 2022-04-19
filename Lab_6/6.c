// Cross the redzone

#include <stdio.h>
#include <stdlib.h>

int main() {
  int a[8] = {0};
  int b[8] = {0};

  // Equals to b[2], but cross the redzone between a and b.
  int c = a[b - a + 2];

  return 0;
}