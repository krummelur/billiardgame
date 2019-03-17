package org.krummelur.raytracer;/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright 2009 Marco Hutter - http://www.jocl.org/
 */

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.jocl.CL.*;

import javafx.scene.shape.Shape3D;
import org.jocl.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * A small JOCL sample.
 */
public class OCLrenderer
{
    boolean shuttingDown = false;
    boolean firstTime = true;
    World world = null;
    Camera camera = null;
    int resolutionX = 900;
    int resolutionY = 900;
    int image[] = new int[resolutionY * resolutionY];
    BufferedImage bi = new BufferedImage( resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB );

    //CL
    cl_context context = null;
    cl_command_queue commandQueue = null;
    cl_kernel kernel = null;
    cl_mem memObjects[] = null;
    cl_program program = null;

    /**
     * The source code of the OpenCL program to execute
     */
    private String programSource;

    /**
     * The entry point of this sample
     */

    int n = resolutionX * resolutionY;
    float rendererWorldData[] = null;
    int rendererScreenData[] = new int[n];
    int renderOutput[] = new int[n];



    public OCLrenderer (World world, Camera camera) {
        this.world = world;
        this.camera = camera;
        programSource = loadFileAsText("CLRenderer.ocl");
        setUpWorldData();
        inititalizeOpenCL();

        //System.exit(0);
        // Create input- and output data


    }


    public boolean isShuttingDown() {
        return shuttingDown;
    }

    void setUpWorldData() {
        //world = new World();
        //world.addObject3d(new Sphere(Vector3.ZERO(),0.9,new Vector3(1,0.6,1)));
        //world.addLight(new PointLight(new Vector3(5,0,0), 10,new Vector3(1,1,1)));
        this.camera = new Camera(new Vector3(2.2,0,0), new Vector3(-1,0,0));
        int numSpheres = world.objects().size();
        int sphereSize = 7;
        int numLights = world.lights().size();
        int lightSize = 7;
        int headerSize = 5;
        int cameraSize = 12;
        float[] wd = new float[headerSize + (numSpheres * sphereSize) + (numLights * lightSize) + cameraSize];
        for (int i = 0; i < world.objects().size(); i++) {
            wd[headerSize + i * sphereSize] = (float) world.objects().get(i).location.x;
            wd[headerSize + i * sphereSize + 1] = (float) world.objects().get(i).location.y;
            wd[headerSize + i * sphereSize + 2] = (float) world.objects().get(i).location.z;
            wd[headerSize + i * sphereSize + 3] = (float) world.objects().get(i).radius;
            wd[headerSize + i * sphereSize + 4] = (float) world.objects().get(i).color.x;
            wd[headerSize + i * sphereSize + 5] = (float) world.objects().get(i).color.y;
            wd[headerSize + i * sphereSize + 6] = (float) world.objects().get(i).color.z;
        }
        for (int i = 0; i < world.lights().size(); i++) {
            wd[headerSize + numSpheres * sphereSize + i * lightSize] = (float) world.lights().get(i).location.x;
            wd[headerSize + numSpheres * sphereSize + i * lightSize + 1] = (float) world.lights().get(i).location.y;
            wd[headerSize + numSpheres * sphereSize + i * lightSize + 2] = (float) world.lights().get(i).location.z;
            wd[headerSize + numSpheres * sphereSize + i * lightSize + 3] = (float) world.lights().get(i).strength;
            wd[headerSize + numSpheres * sphereSize + i * lightSize + 4] = (float) world.lights().get(i).color.x;
            wd[headerSize + numSpheres * sphereSize + i * lightSize + 5] = (float) world.lights().get(i).color.y;
            wd[headerSize + numSpheres * sphereSize + i * lightSize + 6] = (float) world.lights().get(i).color.z;
        }

        {
            {
                int i = 0;
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.location.x();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.location.y();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.location.z();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.direction().x();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.direction().y();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.direction().z();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.lookPlane.up.x();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.lookPlane.up.y();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.lookPlane.up.z();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.lookPlane.right.x();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.lookPlane.right.y();
                wd[headerSize + numSpheres * sphereSize + numLights * lightSize + i++] = (float) camera.lookPlane.right.z();
            }
            {
                int i = 0;
                wd[i++] = headerSize;
                wd[i++] = numSpheres;
                wd[i++] = headerSize + numSpheres * sphereSize;
                wd[i++] = numLights;
                wd[i++] = headerSize + numSpheres * sphereSize + numLights * lightSize;
            }
            float[] wd2 = new float[]{headerSize, numSpheres, headerSize + numSpheres*sphereSize, numLights, headerSize + numSpheres*sphereSize + numLights * lightSize,
                    0,0,0,0.9f,1,0.6f,1,
                    5,0,0,5,1,1,1,
                    1,0,0,-1,0,0, 0,0,1, 0,-1,0
            };
            this.rendererWorldData = wd;
        }
    }

    void inititalizeOpenCL() {
        // The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        commandQueue =
                clCreateCommandQueue(context, device, 0, null);

        // Create the program from the source code
        program = clCreateProgramWithSource(context,
                1, new String[]{ programSource }, null, null);

        // Build the program
        clBuildProgram(program, 0, null, "-cl-unsafe-math-optimizations", null, null);

        // Create the kernel
        kernel = clCreateKernel(program, "RENDERKERNEL", null);

        //Add teardown hook to deinitialize opencl.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> tearDownOpenCL()));
    }

    void tearDownOpenCL () {
        shuttingDown = true;
        clFinish(commandQueue);
        System.out.println("Releasing OpenCL resources");
        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        System.out.println("Releasing Memo 0");
        clReleaseMemObject(memObjects[1]);
        System.out.println("Releasing Memo 1");
        clReleaseKernel(kernel);
        System.out.println("Releasing kernel");
        clReleaseProgram(program);
        System.out.println("Releasing program");
        clReleaseCommandQueue(commandQueue);
        System.out.println("Releasing queue");
        clReleaseContext(context);
        System.out.println("Releasing context");

    }

    int render(RenderWindow window) {
        if(shuttingDown){
            return 100;
        }
        setUpWorldData();
        long startTime = System.currentTimeMillis();


        Pointer srcA = Pointer.to(rendererWorldData);
        Pointer srcB = Pointer.to(rendererScreenData);
        Pointer dst = Pointer.to(renderOutput);

        // Allocate the memory objects for the input- and output data
        cl_mem worldMemObj  = new cl_mem();
        if(firstTime){
            firstTime = false;
            memObjects = new cl_mem[2];
            memObjects[1] = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_float * n, srcB, null);
            memObjects[0] = clCreateBuffer(context,
                    CL_MEM_READ_WRITE,
                    Sizeof.cl_float * n, null, null);
        }

        int[] allocErrRet = {-1000};
        srcA = Pointer.to(rendererWorldData);
        worldMemObj = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * n, srcA, allocErrRet);
        if(allocErrRet[0] != CL_SUCCESS){
            tearDownOpenCL();
            throw new RuntimeException("" + allocErrRet[0]);
        }


        // Set the work-item dimensions
        long global_work_size[] = new long[]{n};
        long local_work_size[] = new long[]{resolutionX/2};

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0,
                Sizeof.cl_mem, Pointer.to(worldMemObj));
        clSetKernelArg(kernel, 1,
                Sizeof.cl_mem, Pointer.to(memObjects[1]));
        clSetKernelArg(kernel, 2,
                Sizeof.cl_mem, Pointer.to(memObjects[0]));

        // Execute the kernel
            clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                    global_work_size, local_work_size, 0, null, null);

        clFinish(commandQueue);
        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[0], CL_TRUE, 0,
                n * Sizeof.cl_float, dst, 0, null, null);

        clReleaseMemObject(worldMemObj);

        //final int[] a = ( (DataBufferInt) bi.getRaster().getDataBuffer() ).getData();
        System.arraycopy(renderOutput, 0, ( (DataBufferInt) bi.getRaster().getDataBuffer() ).getData(), 0, renderOutput.length);

        window.start();


        if (n <= 10)
        {
            System.out.println("Result: "+java.util.Arrays.toString(renderOutput));
        }


        return (int) (System.currentTimeMillis() - startTime);
    }

    String loadFileAsText(String filename) {
        File file = new File(filename);
        FileReader fr = null;
        StringBuilder sb = new StringBuilder();
        try {
               fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);
           String line;
           while((line = br.readLine()) != null){
               sb.append(line).append("\n");
           }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    BufferedImage getImage() {
        return bi;
    }



}