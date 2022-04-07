#include "llvm/Analysis/CFGPrinter.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/IR/Module.h"
#include "llvm/Pass.h"
#include "llvm/Support/FileSystem.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"

using namespace llvm;

namespace {

class ExamplePass : public ModulePass {
 public:
  static char ID;
  ExamplePass() : ModulePass(ID) {}

  bool doInitialization(Module &M) override;
  bool runOnModule(Module &M) override;
};

}  // namespace

char ExamplePass::ID = 0;

bool ExamplePass::doInitialization(Module &M) { return true; }

bool ExamplePass::runOnModule(Module &M) {
  IntegerType *Int32Ty = IntegerType::getInt32Ty(M.getContext());
  Type *VoidTy = Type::getVoidTy(M.getContext());

  for (auto &F : M) {
    if (F.getName() == "main") {
      BasicBlock::iterator IP = F.getEntryBlock().getFirstInsertionPt();
      IRBuilder<> IRB(&(*IP));

      // a
      FunctionType *debugFuncType = FunctionType::get(VoidTy, {Int32Ty}, false);
      FunctionCallee debugCallee =
          M.getOrInsertFunction("debug", debugFuncType);

      IRB.CreateCall(debugCallee, ConstantInt::get(Int32Ty, 9527));

      // b
      Value *newArgv1 = IRB.CreateGlobalString("aesophor is ghost !!!");
      Value *argv1 = IRB.CreateGEP(F.getArg(1), ConstantInt::get(Int32Ty, 1));
      IRB.CreateStore(newArgv1, argv1);

      // c
      F.getArg(0)->replaceAllUsesWith(ConstantInt::get(Int32Ty, 9487));
    }
  }

  return true;
}

static void registerExamplePass(const PassManagerBuilder &,
                                legacy::PassManagerBase &PM) {
  PM.add(new ExamplePass());
}

static RegisterStandardPasses RegisterExamplePass(
    PassManagerBuilder::EP_OptimizerLast, registerExamplePass);

static RegisterStandardPasses RegisterExamplePass0(
    PassManagerBuilder::EP_EnabledOnOptLevel0, registerExamplePass);
