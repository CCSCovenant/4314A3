# 4314A3
This project's goal is to extract all dependencies header #Include in a given directory.

Includes parsing and reformatting of .ta files given by scrML and understand.

Benchmarks each extraction

# how to run this project
## how to run extraction
## understand
replace understand.ta with your raw.ta file from understand.

## prepare before run include and srcML
1. place gcc-12.2.0 folder in the ./
- .....
- src
- gcc-12.2.0
- ....
2. install srcMl run following comment in the terminal  
```srcml gcc-12.2.0 --verbose -o srcml_gcc.xml```   
```srcml --xpath "//cpp:include" srcml_gcc.xml > scrML_query_result.xml```  
```srcml --xpath "//src:call" srcml_gcc.xml > scrML_query_call_result.xml```  
```srcml --xpath "//src:function_decl" srcml_gcc.xml > scrML_query_decl_result.xml```  
```srcml --xpath "//src:decl" srcml_gcc.xml > scrML_query_var_decl_result.xml```  

save the result xmlfile in the ./
replace line1 of output xml to   
   ```<?xml version="1.0"?>```   
in order to make the parser run.  
## include 
run ./DependencyExtraction/IncludeExtraction_srcML.java

## srcML
run ./scrMLExtraction/srcMLExtraction.java

## how to run benchmarking.
1. sampling by with ./MethodBenchmarking/TApreprocess.java. it will choose all dependency from target files in the ground truth
2. you will have local.ta file in ./LocalTA folder.
3. run ./MethodBenchmarking/Benchmarking.java
