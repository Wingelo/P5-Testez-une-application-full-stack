describe('Test the not found component', () => {
  it('Should return true when the application doesn\'t exist', () => {
    cy.visit('/not-found');
    cy.contains('Page not found').should('be.visible');
  })
})
