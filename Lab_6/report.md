# Software Testing Lab 6 Report

## Environment

* `gcc`: `gcc (Ubuntu 9.4.0-1ubuntu1~20.04.1) 9.4.0`
* `valgrind`: `valgrind-3.15.0`

## Heap out-of-bound read/write

```c
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
```

### ASan report

```shell
=================================================================
==2331==ERROR: AddressSanitizer: heap-buffer-overflow on address 0x602000000030 at pc 0x5651a8a61242 bp 0x7ffd2b45d360 sp 0x7ffd2b45d350
WRITE of size 4 at 0x602000000030 thread T0
    #0 0x5651a8a61241 in main (/home/david/Software-Testing/Lab_6/1.out+0x1241)
    #1 0x7fbd1d5f70b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)
    #2 0x5651a8a6112d in _start (/home/david/Software-Testing/Lab_6/1.out+0x112d)

Address 0x602000000030 is a wild pointer.
SUMMARY: AddressSanitizer: heap-buffer-overflow (/home/david/Software-Testing/Lab_6/1.out+0x1241) in main
Shadow bytes around the buggy address:
  0x0c047fff7fb0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7fc0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7fd0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7fe0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7ff0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
=>0x0c047fff8000: fa fa 00 fa fa fa[fa]fa fa fa fa fa fa fa fa fa
  0x0c047fff8010: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8020: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8030: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8040: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8050: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
Shadow byte legend (one shadow byte represents 8 application bytes):
  Addressable:           00
  Partially addressable: 01 02 03 04 05 06 07
  Heap left redzone:       fa
  Freed heap region:       fd
  Stack left redzone:      f1
  Stack mid redzone:       f2
  Stack right redzone:     f3
  Stack after return:      f5
  Stack use after scope:   f8
  Global redzone:          f9
  Global init order:       f6
  Poisoned by user:        f7
  Container overflow:      fc
  Array cookie:            ac
  Intra object redzone:    bb
  ASan internal:           fe
  Left alloca redzone:     ca
  Right alloca redzone:    cb
  Shadow gap:              cc
==2331==ABORTING
```

### valgrind report

```shell
==3282== Memcheck, a memory error detector
==3282== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==3282== Using Valgrind-3.15.0 and LibVEX; rerun with -h for copyright info
==3282== Command: ./1.out
==3282==
==3282== Invalid write of size 4
==3282==    at 0x10918B: main (in /home/david/Software-Testing/Lab_6/1.out)
==3282==  Address 0x4a48060 is 16 bytes after a block of size 16 in arena "client"
==3282==
==3282== Invalid read of size 4
==3282==    at 0x109195: main (in /home/david/Software-Testing/Lab_6/1.out)
==3282==  Address 0x4a48060 is 16 bytes after a block of size 16 in arena "client"
==3282==
==3282==
==3282== HEAP SUMMARY:
==3282==     in use at exit: 0 bytes in 0 blocks
==3282==   total heap usage: 1 allocs, 1 frees, 8 bytes allocated
==3282==
==3282== All heap blocks were freed -- no leaks are possible
==3282==
==3282== For lists of detected and suppressed errors, rerun with: -s
==3282== ERROR SUMMARY: 2 errors from 2 contexts (suppressed: 0 from 0)
```

## Stack out-of-bounds read/write

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
  int a[8] = {0};
  a[8] = 0xabcd;  // out-of-bound write
  int b = a[8];   // out-of-bound read
  return 0;
}
```

### ASan report

```shell
==2365==ERROR: AddressSanitizer: stack-buffer-overflow on address 0x7ffe2b649af0 at pc 0x55dc3d00a336 bp 0x7ffe2b649a90 sp 0x7ffe2b649a80
WRITE of size 4 at 0x7ffe2b649af0 thread T0
    #0 0x55dc3d00a335 in main (/home/david/Software-Testing/Lab_6/2.out+0x1335)
    #1 0x7fb3defcd0b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)
    #2 0x55dc3d00a12d in _start (/home/david/Software-Testing/Lab_6/2.out+0x112d)

Address 0x7ffe2b649af0 is located in stack of thread T0 at offset 64 in frame
    #0 0x55dc3d00a1f8 in main (/home/david/Software-Testing/Lab_6/2.out+0x11f8)

  This frame has 1 object(s):
    [32, 64) 'a' (line 8) <== Memory access at offset 64 overflows this variable
HINT: this may be a false positive if your program uses some custom stack unwind mechanism, swapcontext or vfork
      (longjmp and C++ exceptions *are* supported)
SUMMARY: AddressSanitizer: stack-buffer-overflow (/home/david/Software-Testing/Lab_6/2.out+0x1335) in main
Shadow bytes around the buggy address:
  0x1000456c1300: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1310: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1320: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1330: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1340: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
=>0x1000456c1350: 00 00 00 00 00 00 f1 f1 f1 f1 00 00 00 00[f3]f3
  0x1000456c1360: f3 f3 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1370: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1380: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c1390: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x1000456c13a0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
Shadow byte legend (one shadow byte represents 8 application bytes):
  Addressable:           00
  Partially addressable: 01 02 03 04 05 06 07
  Heap left redzone:       fa
  Freed heap region:       fd
  Stack left redzone:      f1
  Stack mid redzone:       f2
  Stack right redzone:     f3
  Stack after return:      f5
  Stack use after scope:   f8
  Global redzone:          f9
  Global init order:       f6
  Poisoned by user:        f7
  Container overflow:      fc
  Array cookie:            ac
  Intra object redzone:    bb
  ASan internal:           fe
  Left alloca redzone:     ca
  Right alloca redzone:    cb
  Shadow gap:              cc
==2365==ABORTING
```

### valgrind report

```shell
==3323== Memcheck, a memory error detector
==3323== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==3323== Using Valgrind-3.15.0 and LibVEX; rerun with -h for copyright info
==3323== Command: ./2.out
==3323==
==3323==
==3323== HEAP SUMMARY:
==3323==     in use at exit: 0 bytes in 0 blocks
==3323==   total heap usage: 0 allocs, 0 frees, 0 bytes allocated
==3323==
==3323== All heap blocks were freed -- no leaks are possible
==3323==
==3323== For lists of detected and suppressed errors, rerun with: -s
==3323== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 0 from 0)
```

## Global out-of-bounds read/write

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int a[8] = {0};

int main() {
  a[8] = 0xabcd;  // out-of-bound write
  int b = a[8];   // out-of-bound read
  return 0;
}
```

### ASan report

```shell
=================================================================
==2428==ERROR: AddressSanitizer: global-buffer-overflow on address 0x556dadb680c0 at pc 0x556dadb65207 bp 0x7fff1ed15ea0 sp 0x7fff1ed15e90
WRITE of size 4 at 0x556dadb680c0 thread T0
    #0 0x556dadb65206 in main (/home/david/Software-Testing/Lab_6/3.out+0x1206)
    #1 0x7f1fab5230b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)
    #2 0x556dadb6510d in _start (/home/david/Software-Testing/Lab_6/3.out+0x110d)

0x556dadb680c0 is located 0 bytes to the right of global variable 'a' defined in '3.c:7:5' (0x556dadb680a0) of size 32
SUMMARY: AddressSanitizer: global-buffer-overflow (/home/david/Software-Testing/Lab_6/3.out+0x1206) in main
Shadow bytes around the buggy address:
  0x0aae35b64fc0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b64fd0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b64fe0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b64ff0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b65000: 00 00 00 00 00 00 00 00 f9 f9 f9 f9 f9 f9 f9 f9
=>0x0aae35b65010: 00 00 00 00 00 00 00 00[f9]f9 f9 f9 00 00 00 00
  0x0aae35b65020: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b65030: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b65040: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b65050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0aae35b65060: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
Shadow byte legend (one shadow byte represents 8 application bytes):
  Addressable:           00
  Partially addressable: 01 02 03 04 05 06 07
  Heap left redzone:       fa
  Freed heap region:       fd
  Stack left redzone:      f1
  Stack mid redzone:       f2
  Stack right redzone:     f3
  Stack after return:      f5
  Stack use after scope:   f8
  Global redzone:          f9
  Global init order:       f6
  Poisoned by user:        f7
  Container overflow:      fc
  Array cookie:            ac
  Intra object redzone:    bb
  ASan internal:           fe
  Left alloca redzone:     ca
  Right alloca redzone:    cb
  Shadow gap:              cc
==2428==ABORTING
```

### valgrind report

```shell
==3372== Memcheck, a memory error detector
==3372== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==3372== Using Valgrind-3.15.0 and LibVEX; rerun with -h for copyright info
==3372== Command: ./3.out
==3372==
==3372==
==3372== HEAP SUMMARY:
==3372==     in use at exit: 0 bytes in 0 blocks
==3372==   total heap usage: 0 allocs, 0 frees, 0 bytes allocated
==3372==
==3372== All heap blocks were freed -- no leaks are possible
==3372==
==3372== For lists of detected and suppressed errors, rerun with: -s
==3372== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 0 from 0)
```

## Use-after-free

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
  int *a = (int *)malloc(4);
  free(a);
  int b = a[0];  // use-after-free
  return 0;
}
```

### ASan report

```shell
==2559==ERROR: AddressSanitizer: heap-use-after-free on address 0x602000000010 at pc 0x55f557989226 bp 0x7ffc3d17f4c0 sp 0x7ffc3d17f4b0
READ of size 4 at 0x602000000010 thread T0
    #0 0x55f557989225 in main (/home/david/Software-Testing/Lab_6/4.out+0x1225)
    #1 0x7fd52fad50b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)
    #2 0x55f55798910d in _start (/home/david/Software-Testing/Lab_6/4.out+0x110d)

0x602000000010 is located 0 bytes inside of 4-byte region [0x602000000010,0x602000000014)
freed by thread T0 here:
    #0 0x7fd52fdb040f in __interceptor_free ../../../../src/libsanitizer/asan/asan_malloc_linux.cc:122
    #1 0x55f5579891ee in main (/home/david/Software-Testing/Lab_6/4.out+0x11ee)
    #2 0x7fd52fad50b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)

previously allocated by thread T0 here:
    #0 0x7fd52fdb0808 in __interceptor_malloc ../../../../src/libsanitizer/asan/asan_malloc_linux.cc:144
    #1 0x55f5579891de in main (/home/david/Software-Testing/Lab_6/4.out+0x11de)
    #2 0x7fd52fad50b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)

SUMMARY: AddressSanitizer: heap-use-after-free (/home/david/Software-Testing/Lab_6/4.out+0x1225) in main
Shadow bytes around the buggy address:
  0x0c047fff7fb0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7fc0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7fd0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7fe0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x0c047fff7ff0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
=>0x0c047fff8000: fa fa[fd]fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8010: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8020: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8030: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8040: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
  0x0c047fff8050: fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa fa
Shadow byte legend (one shadow byte represents 8 application bytes):
  Addressable:           00
  Partially addressable: 01 02 03 04 05 06 07
  Heap left redzone:       fa
  Freed heap region:       fd
  Stack left redzone:      f1
  Stack mid redzone:       f2
  Stack right redzone:     f3
  Stack after return:      f5
  Stack use after scope:   f8
  Global redzone:          f9
  Global init order:       f6
  Poisoned by user:        f7
  Container overflow:      fc
  Array cookie:            ac
  Intra object redzone:    bb
  ASan internal:           fe
  Left alloca redzone:     ca
  Right alloca redzone:    cb
  Shadow gap:              cc
==2559==ABORTING
```

### valgrind report

```shell
==3403== Memcheck, a memory error detector
==3403== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==3403== Using Valgrind-3.15.0 and LibVEX; rerun with -h for copyright info
==3403== Command: ./4.out
==3403==
==3403== Invalid read of size 4
==3403==    at 0x109193: main (in /home/david/Software-Testing/Lab_6/4.out)
==3403==  Address 0x4a48040 is 0 bytes inside a block of size 4 free'd
==3403==    at 0x483CA3F: free (in /usr/lib/x86_64-linux-gnu/valgrind/vgpreload_memcheck-amd64-linux.so)
==3403==    by 0x10918E: main (in /home/david/Software-Testing/Lab_6/4.out)
==3403==  Block was alloc'd at
==3403==    at 0x483B7F3: malloc (in /usr/lib/x86_64-linux-gnu/valgrind/vgpreload_memcheck-amd64-linux.so)
==3403==    by 0x10917E: main (in /home/david/Software-Testing/Lab_6/4.out)
==3403==
==3403==
==3403== HEAP SUMMARY:
==3403==     in use at exit: 0 bytes in 0 blocks
==3403==   total heap usage: 1 allocs, 1 frees, 4 bytes allocated
==3403==
==3403== All heap blocks were freed -- no leaks are possible
==3403==
==3403== For lists of detected and suppressed errors, rerun with: -s
==3403== ERROR SUMMARY: 1 errors from 1 contexts (suppressed: 0 from 0)
```

## Use-after-return

```c
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
```

### ASan report

```shell
AddressSanitizer:DEADLYSIGNAL
=================================================================
==3053==ERROR: AddressSanitizer: SEGV on unknown address 0x000000000000 (pc 0x5585c395c3b6 bp 0x7ffc17294760 sp 0x7ffc17294750 T0)
==3053==The signal is caused by a READ memory access.
==3053==Hint: address points to the zero page.
    #0 0x5585c395c3b5 in main (/home/david/Software-Testing/Lab_6/5.out+0x13b5)
    #1 0x7f44848690b2 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x240b2)
    #2 0x5585c395c12d in _start (/home/david/Software-Testing/Lab_6/5.out+0x112d)

AddressSanitizer can not provide additional info.
SUMMARY: AddressSanitizer: SEGV (/home/david/Software-Testing/Lab_6/5.out+0x13b5) in main
==3053==ABORTING
```

### valgrind report

```shell
==3437== Memcheck, a memory error detector
==3437== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==3437== Using Valgrind-3.15.0 and LibVEX; rerun with -h for copyright info
==3437== Command: ./5.out
==3437==
==3437== Invalid read of size 4
==3437==    at 0x1091B5: main (in /home/david/Software-Testing/Lab_6/5.out)
==3437==  Address 0x0 is not stack'd, malloc'd or (recently) free'd
==3437==
==3437==
==3437== Process terminating with default action of signal 11 (SIGSEGV)
==3437==  Access not within mapped region at address 0x0
==3437==    at 0x1091B5: main (in /home/david/Software-Testing/Lab_6/5.out)
==3437==  If you believe this happened as a result of a stack
==3437==  overflow in your program's main thread (unlikely but
==3437==  possible), you can try to increase the size of the
==3437==  main thread stack using the --main-stacksize= flag.
==3437==  The main thread stack size used in this run was 8388608.
==3437==
==3437== HEAP SUMMARY:
==3437==     in use at exit: 0 bytes in 0 blocks
==3437==   total heap usage: 0 allocs, 0 frees, 0 bytes allocated
==3437==
==3437== All heap blocks were freed -- no leaks are possible
==3437==
==3437== For lists of detected and suppressed errors, rerun with: -s
==3437== ERROR SUMMARY: 1 errors from 1 contexts (suppressed: 0 from 0)
[1]    3437 segmentation fault  valgrind ./5.out
```
