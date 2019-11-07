import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavaEditorAgent {
	public static void premain(String args, Instrumentation inst) {
		inst.addTransformer(new Transformer());
	}
}

class Transformer implements ClassFileTransformer {
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectedDomain, byte[] classfileBuffer) throws IllegalClassFormatException{
		if(className == null ||
		   className.startsWith("java") ||
		   className.startsWith("sun") ||
		   className.startsWith("javax") ||
		   className.startsWith("com") ||
		   className.startsWith("jdk") ||
		   className.startsWith("org")) {
			return classfileBuffer;
		}
		System.out.println("*Loaded Class: " + className);
		ClassPool pool = new ClassPool(true);
		try {
			CtClass currentClass = pool.get(className);
			CtMethod[] methods = currentClass.getDeclaredMethods();
			for(CtBehavior redefineMethod : methods) {
				redefineMethod.insertBefore("System.out.println(\"*" + className + "." + redefineMethod.getName() + " called\");");
			}
			return currentClass.toBytecode();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classfileBuffer;
	}
}