# File API Microservice

The File API microservice is a core component of the Microservices Music Metadata Enrichment System, responsible for handling song file uploads and managing their storage. It allows users to upload song files via the File API, stores the files either locally or in Amazon S3, and maintains metadata about the files in the database.

## Overview

The File API microservice serves as an entry point for users to upload song files to the system. It ensures that uploaded files are stored securely, maintains metadata about the files, and allows other microservices to retrieve file information when needed.

## Functionality

- **File Upload**: The File API provides an endpoint to accept and store song files sent by clients using the File API.

- **File Storage**: Uploaded files are stored either locally or in the Amazon S3 storage service, based on system configuration.

- **Metadata Storage**: Metadata about the uploaded files, such as the file name, storage type (S3/local), and path, are stored in the database.

## Endpoints

The File API microservice exposes the following RESTful endpoint:

- `POST /files/upload`: Upload a song file to the system.
- `GET /files/download/{id}`: Download a song file from the storage by its ID.

## Database

The File API microservice stores file metadata in a chosen SQL or NoSQL database, as per the system's configuration. The database schema contains fields to store details about the uploaded files, such as the file name, storage type, and path.

## Dependencies

- **Spring Boot**: The microservice is built using the Spring Boot framework, providing a lightweight and efficient foundation.
- **Amazon S3 SDK**: If S3 storage is enabled, the File API uses the Amazon S3 SDK to interact with the S3 service.
- **Kafka**: After successful upload, service adds file id in Kafka queue.

## How to Use

Developers and clients can interact with the File API microservice by making HTTP requests to the `POST /api/files/upload` endpoint. Upon successful file upload, the file's metadata, such as the file name, storage type, and path, is stored in the database for later retrieval.

## Contribution

Contributions to the File API microservice or the entire Microservices Music Metadata Enrichment System are welcomed. Developers can contribute by opening issues, submitting pull requests, or improving the documentation.