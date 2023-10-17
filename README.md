# Assignment: HTTP Server with REST API for Binary File CRUD Operations

**Objective**: Create a simple HTTP server using Go that provides a REST API for performing CRUD operations on a single
resource type, which will be stored in a binary file.

This task is designed to evaluate multiple aspects, including API development, file I/O, data serialization, error
handling, testing, possibly concurrent programming and in end the proof basic knowledge of work with Docker, which
should provide a comprehensive view of the candidate's skills in a more constructive and straightforward manner. This
also opens a healthy discussion post-task regarding the approaches, potential improvements, and scaling possibilities,
making the interview process constructive and informative for both parties.

The assigment can be implemented in any language, but we recommend using Go. Anyway, implementation in languages like
Haskell, brainfuck, COBOL, Erlang, Closure etc. are sort of pointless if we can't evaluate them.

The implementation can be done by 383 lines of formatted Go code just by using standard library. This is not a
condition, rather information that it's not a large project. You should add tests and some doc comments, so your amount
of lines will be bigger. On the other hand, don't overdo. No one wants you to waste your free time.

## Resource Type

Define a Record type with the following fields:

- **`ID`**: an int64
- **`IntValue`**: an int64
- **`StrValue`**: a string, fixed size of 64 bytes
- **`BoolValue`**: a bool
- **`TimeValue`**: a time.Time

## API Endpoints

- **`POST /records`**: Create a new record
- **`GET /records/{id}`**: Retrieve a record by ID
- **`PUT /records/{id}`**: Update a record by ID
- **`DELETE /records/{id}`**: Delete a record by ID
- **`GET /readyz`**: Readiness probe, signaling the service is ready to accept requests

```json
{
  "IntValue": 42,
  "StrValue": "foo",
  "BoolValue": true,
  "TimeValue": "2023-10-10T21:57:00+02:00"
}
```

Time is formatted according to [RFC3339](https://tools.ietf.org/html/rfc3339).

## Binary File Structure

- Each record should be stored in a binary file.
- The `ID` of the record will be determined by its position in the file.
- The first record will have an ID of 1, the second record will have an ID of 2, etc.
- The `ID` field (int64) occupies first **8 bytes** and if the record is deleted, the `ID` field will be set to 0.
- The `IntValue` field (int64) occupies the next **8 bytes** and should be stored as a **little-endian int64**, same as
  the `ID`
  field.
- The `StrValue` fields has fixed size **64 bytes**, and any unused bytes should be filled with null bytes (0x00) at the
  end. The encoding is
  UTF-8.
- The `BoolValue` field occupies the next byte and should be stored as a **single byte**. 0 = false, 0 != true.
- The `TimeValue` field uses Go's default `time.Time` binary serialization(*). See
  [implementation](https://pkg.go.dev/time#Time.MarshalBinary) for more information, as there is no other specification
  for the format. Because the size of its output isn't fixed, add 0x00 suffix to fit **16 bytes**.
- Every record should end with a newline character `\n` using **1 byte**.
- You may reuse the space of deleted records, but you don't have to.

This repository contains example data stored in `records.bin`.

*) This is a little penalty for not using Go for the assignment. Here is a reimplementation of the serialization in
Python [examples/bin_time/main.py](examples/bin_time/main.py) to demonstrate it's possible to implement it in any other
language.

## IntelliJ HTTP Client

The repository also contains a `docker-compose.yml` file with `jetbrains/intellij-http-client` image.
Feel free to verify your implementation using the HTTP client. You shouldn't rely on the provided tests only.

## Service in a context

The service should be able to run in a Docker container, thus the Dockerfile should be provided. Determined by the
persistent layer, the service is expected to run in one instance and won't be distributed. Is expected that the service
will be used by multiple clients, and then it should be able to handle multiple concurrent requests safely.

## Criteria

- **`Correctness`**: The application should correctly implement the CRUD operations.
- **`Error Handling`**: The application should gracefully handle possible error scenarios.
- **`Concurrency`**: Consider how the application will handle concurrent requests.
- **`Testing`**: Include tests demonstrating that your application works as expected.
- **`Documentation`**: Provide clear documentation of your application.

## Evaluation

- **`Correct and Efficient I/O Operations`**: Ability to correctly read/write to a binary file.
- **`API Implementation`**: The correctness of the REST API.
- **`Code Quality`**: Cleanliness, readability, and maintainability of the code.
- **`Concurrency Management`**: Safeguarding file operations against concurrent access.
- **`Testing Quality`**: Comprehensive and clear tests.

## Handover

You may send an email, or share a git repository on GitHub or GitLab.

- GitLab: [@prochac.dataddo](https://gitlab.com/prochac.dataddo) (preferred) or [@prochac](https://gitlab.com/prochac)
- GitHub: [@prochac](https://github.com/prochac)

## Disclaimer

This service is truly for interview purposes only. It is not intended to be used in production, and it is not intended
to be used as a reference for production code, by Dataddo or by anyone else. You don't do free labor for Dataddo here :D