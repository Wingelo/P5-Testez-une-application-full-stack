describe('User e2e register test', () => {
  it('Register', () => {
    cy.visit('/register')

    cy.intercept('POST', 'api/auth/register', {
      statusCode: 200,
      body: {
        firstName: 'firstname',
        lastName: 'lastname',
        email: 'yoga@studio.com',
        password: 'password'
      },
    }).as('registerRequest')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('input[formControlName=email]').type('yoga@studio.com')
    cy.get('input[formControlName=password]').type('test!1234')

    cy.get('button[type=submit]').click()

    cy.wait('@registerRequest').its('request.body').should('deep.equal', {
      firstName: 'John',
      lastName: 'Doe',
      email: 'yoga@studio.com',
      password: 'test!1234'
    })

    cy.url().should('include', '/login')
  })
})
