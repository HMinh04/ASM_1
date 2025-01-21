import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  template: `
    <div class="max-w-sm mx-auto mt-20 p-6 border border-gray-300 rounded-lg shadow-md">
      <h2 class="text-2xl font-semibold mb-6 text-center">Register</h2>
      <form (ngSubmit)="onSubmit()">
        <div class="mb-4">
          <label for="username" class="block text-sm font-medium text-gray-700">Username:</label>
          <input
            type="text"
            id="username"
            name="username"
            [(ngModel)]="registerData.username"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div class="mb-4">
          <label for="password" class="block text-sm font-medium text-gray-700">Password:</label>
          <input
            type="password"
            id="password"
            name="password"
            [(ngModel)]="registerData.password"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div class="mb-4">
          <label for="email" class="block text-sm font-medium text-gray-700">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            [(ngModel)]="registerData.email"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div class="mb-4">
          <label for="firstName" class="block text-sm font-medium text-gray-700">First Name:</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            [(ngModel)]="registerData.firstName"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div class="mb-6">
          <label for="lastName" class="block text-sm font-medium text-gray-700">Last Name:</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            [(ngModel)]="registerData.lastName"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          type="submit"
          class="w-full px-4 py-2 bg-green-600 text-white font-semibold rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
        >
          Register
        </button>
      </form>
      <p class="mt-4 text-center text-sm">
        Already have an account? <a routerLink="/login" class="text-blue-600 hover:underline cursor-pointer">Login here</a>
      </p>
    </div>
  `
})
export class RegisterComponent {
  registerData = {
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: ''
  };

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  onSubmit() {
    this.http.post('http://localhost:8080/register', this.registerData)
      .subscribe({
        next: (response) => {
          Swal.fire({
            title: 'Success!',
            text: 'Registration successful',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then((result) => {
            if (result.isConfirmed) {
              this.router.navigate(['/login']);
            }
          });
        },
        error: (error) => {
          Swal.fire({
            title: 'Error!',
            text: 'Registration failed',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
  }
}